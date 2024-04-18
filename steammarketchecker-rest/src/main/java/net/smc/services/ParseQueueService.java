package net.smc.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.smc.dto.ParseQueueDto;
import net.smc.dto.dtofromjson.LotFromJsonDto;
import net.smc.dto.dtofromjson.ParseResultForLot;
import net.smc.dto.dtofromjson.ParseResultForSteamItem;
import net.smc.dto.dtofromjson.SteamItemFromJsonDto;
import net.smc.entities.Lot;
import net.smc.entities.LotSticker;
import net.smc.entities.ParseQueue;
import net.smc.entities.SteamItem;
import net.smc.enums.SteamItemType;
import net.smc.readers.ParseQueueReader;
import net.smc.readers.SteamItemReader;
import net.smc.repositories.LotRepository;
import net.smc.repositories.LotStickerRepository;
import net.smc.repositories.ParseQueueRepository;
import net.smc.repositories.SteamItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ParseQueueService {

    @Value("${queue.max-attempts}")
    private Integer maxQueueAttempts;

    @Value("${default-parse-period.steam-item}")
    private Integer defaultSteamItemParsePeriod;
    private final ParseQueueReader parseQueueReader;
    private final ParseQueueRepository parseQueueRepository;
    private final SteamItemRepository steamItemRepository;
    private final SteamItemReader steamItemReader;
    private final LotRepository lotRepository;
    private final LotStickerRepository lotStickerRepository;
    private final ParserService parserService;

    @Scheduled(fixedDelayString = "${scheduled.parse-queue}", initialDelay = 1000)
    public void parseActiveNamesByPeriod() {
        List<ParseQueue> allActualParseQueues = parseQueueRepository.findAllByArchiveOrderByImportanceDescIdAsc(false);
        if (allActualParseQueues.size() > 0) {
            ParseQueue oneActualQueue = allActualParseQueues.get(0);
            switch (oneActualQueue.getParseType()) {
                case LOT -> {
                    // парсим новые данные на это название
                    lotParsing(oneActualQueue);
                }
                case SKIN, STICKER -> {
                    steamItemParsing(oneActualQueue);
                }
            }
        }
    }

    private void deleteOldLotsIfNeeded(ParseQueue oneActualQueue) {
        List<Lot> oldLots = lotRepository.findAllByLotParseTarget(oneActualQueue.getParseTarget());
        List<LotSticker> oldLotStickers = lotStickerRepository.findAllByLotParseTarget(oneActualQueue.getParseTarget());
        if (oldLots.size() > 0) lotRepository.deleteAll(oldLots);
        if (oldLotStickers.size() > 0) lotStickerRepository.deleteAll(oldLotStickers);
    }

    private void lotParsing(ParseQueue oneActualQueue) {
        // Парсим данные о лотах со стикерами со steamMarket
        ParseResultForLot parseResultForLot = parserService.parseListingFromApi(oneActualQueue.getParseUrl());
        List<LotFromJsonDto> lotsWithStickers = parseResultForLot.getLotWithStickersFromJsonDtoList();
        // Проверяем, успешен ли ответ
        if (parseResultForLot.isConnectSuccessful()) {
            // данные про старые лоты с таким же названием должны быть удалены
            deleteOldLotsIfNeeded(oneActualQueue);
            // Проверяем, что данные пришли нормальные (непустой список)
            // (коннект может быть удачным, но с пустым списком - это нормальная ситуация, ничего не делаем)
            if (lotsWithStickers != null && !lotsWithStickers.isEmpty()) {
                SteamItem steamItem = steamItemRepository.findById(oneActualQueue.getSteamItemId())
                        .orElseThrow(() -> new RuntimeException("Не найден steamItem в момент создания лота"));
                // Находим в базе все упоминания найденных стикеров
                Map<String, Long> mapStickerIdByName = getAllSteamItemIdsByNamesForAllStickers(lotsWithStickers);
                // Найденные стикеры достаем одним запросом и пихаем в мапу
                Map<Long, SteamItem> mapStickerById = steamItemRepository.findAllById(mapStickerIdByName.values())
                        .stream().collect(Collectors.toMap(SteamItem::getId, Function.identity()));
                // Пробегаемся по каждому лоту.
                // С каждого лота создается одна запись в таблицу лотов
                // А также одна/несколько записей в steamItem (как стикер)
                // Одна/несколько записей в lotSticker
                for (LotFromJsonDto lotFromJson : lotsWithStickers) {
                    Lot lot = new Lot(oneActualQueue.getParseTarget(), lotFromJson, steamItem);
                    lotRepository.saveAndFlush(lot);
                    createSteamItemsAndLotStickerForStickers(lotFromJson, lot, mapStickerIdByName, mapStickerById);
                }
            }
            // если всё успешно
            oneActualQueue.setArchive(true);
            parseQueueRepository.saveAndFlush(oneActualQueue);
            System.out.println(String.format("ParseQueue with id %s. Success!", oneActualQueue.getId()));
        } else { // Иначе произошел сбой - откладываем задачу на потом
            putQueueAside(oneActualQueue);
            System.out.println(String.format("ParseQueue with id %s. Fail", oneActualQueue.getId()));
        }
    }

    private Map<String, Long> getAllSteamItemIdsByNamesForAllStickers(List<LotFromJsonDto> lotsWithStickers) {
        List<String> allStickersUnprocessed = lotsWithStickers.stream().map(LotFromJsonDto::getStickersAsString).toList();
        List<String> allStickersProcessed = new ArrayList<>();
        allStickersUnprocessed.forEach(multipleStickersAsString ->
                splitStickers(multipleStickersAsString)
                        .forEach(stickerName -> allStickersProcessed.add(stickerName + "_sticker")));
        return steamItemReader.getAllSteamItemIdByName(allStickersProcessed);
    }

    private void createSteamItemsAndLotStickerForStickers(LotFromJsonDto lotFromJson, Lot lot,
                                                          Map<String, Long> mapStickerIdByName,
                                                          Map<Long, SteamItem> mapStickerById) {
        List<LotSticker> lotStickers = new ArrayList<>();
        List<String> stickerNames = splitStickers(lotFromJson.getStickersAsString());
        stickerNames.forEach(stickerName -> {
            // Если такой стикер уже существует, то берем его id. Создаем соответствующий lotSticker.
            Long stickerId = mapStickerIdByName.get(stickerName + "_sticker");
            if (stickerId != null) {
                LotSticker lotSticker = new LotSticker(lot.getId(), lot.getLotParseTarget(), mapStickerById.get(stickerId));
                lotStickers.add(lotSticker);
            } else { // Иначе - создаем новую запись в бд с таким стикером. Затем создаем соответствующий lotSticker.
                // todo тут нужно настраивать importance
                SteamItem newSticker = new SteamItem(stickerName + "_sticker", SteamItemType.STICKER, defaultSteamItemParsePeriod);
                steamItemRepository.saveAndFlush(newSticker);
                // Так как может возникнуть ситуация, когда в одном скине одинаковые стикеры,
                // То каждый новый стикер добавим в эту мапу
                mapStickerIdByName.put(newSticker.getName(), newSticker.getId());
                mapStickerById.put(newSticker.getId(), newSticker);
                LotSticker lotSticker = new LotSticker(lot.getId(), lot.getLotParseTarget(), newSticker);
                lotStickers.add(lotSticker);
            }
        });
        lotStickerRepository.saveAllAndFlush(lotStickers);
    }

    private List<String> splitStickers(String stickersAsString) {
        String[] stringArray = stickersAsString.split(", ");
        List<String> result = new ArrayList<>();
        if (Arrays.stream(stringArray).anyMatch(e -> e.contains("Champion"))) {
            for (int i = 0; i < stringArray.length; i++) {
                if (stringArray[i].contains("Champion") && !stringArray[i].contains(" (")) {
                    result.remove(stringArray[i-1]);
                    result.add(stringArray[i-1] + ", " + stringArray[i]);
                } else {
                    result.add(stringArray[i]);
                }
            }
            return result;
        } else {
            return List.of(stringArray);
        }
    }

    private void steamItemParsing(ParseQueue oneActualQueue) {
        // Парсим данные о цене скина/стикера со steamMarket
        ParseResultForSteamItem parseResultForSteamItem = parserService.parseSteamItemFromApi(oneActualQueue.getParseUrl());
        SteamItemFromJsonDto steamItemFromJsonDto = parseResultForSteamItem.getSteamItemFromJsonDto();
        // Если данные пришли нормальные
        if (parseResultForSteamItem.isConnectSuccessful()
                && steamItemFromJsonDto != null
                && steamItemFromJsonDto.getMinPrice() != null) {
            List<SteamItem> steamItemsWithThisName = steamItemRepository.findAllByName(oneActualQueue.getParseTarget());
            // Если такой скин/стикер не существуют
            if (steamItemsWithThisName.size() == 0) {
                // Создаем в базе новый steamItem. Проставляем ему название, цены, тип
                // (этот вариант не должен происходить - закрыто для надежности)
                SteamItem newSteamItem = new SteamItem(
                        oneActualQueue.getParseTarget(),
                        steamItemFromJsonDto.getMinPrice(),
                        SteamItemType.valueOf(oneActualQueue.getParseType().toString()),
                        defaultSteamItemParsePeriod);
                steamItemRepository.saveAndFlush(newSteamItem);
            } else if (steamItemsWithThisName.size() == 1) { // Если такой скин/стикер уже существуют.
                // Обновляем в базе старый steamItem. Проставляем ему цены.
                steamItemsWithThisName.get(0).updateSteamItemPrices(steamItemFromJsonDto.getMinPrice());
                steamItemRepository.saveAllAndFlush(steamItemsWithThisName);
            } else {
                // не должно случиться по constraint
                throw new RuntimeException("Произошло нарушение целостности steamItem-таблицы");
            }
            // если всё успешно
            oneActualQueue.setArchive(true);
            parseQueueRepository.saveAndFlush(oneActualQueue);
            System.out.println(String.format("ParseQueue with id %s. Success!", oneActualQueue.getId()));
        } else { // Иначе произошел сбой - откладываем задачу на потом
            putQueueAside(oneActualQueue);
            System.out.println(String.format("ParseQueue with id %s. Fail", oneActualQueue.getId()));
        }
    }

    private void putQueueAside(ParseQueue oneActualQueue) {
        oneActualQueue.setAttempt(oneActualQueue.getAttempt() + 1);
        //Понизим приоритет - чтобы одна очередь не занимала всё
        // todo - возможно, так делать не стоит - лоты улетают в небытие
        oneActualQueue.setImportance(oneActualQueue.getImportance() - 1);
        if (oneActualQueue.getAttempt() > maxQueueAttempts) {
            parseQueueRepository.delete(oneActualQueue);
        } else {
            parseQueueRepository.saveAndFlush(oneActualQueue);
        }
    }

    // api
    public List<ParseQueueDto> getAllParseQueues(Boolean archive) {
        return parseQueueReader.getAllParseQueues(archive);
    }
}
