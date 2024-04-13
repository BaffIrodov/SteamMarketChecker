package net.smc.services;

import lombok.RequiredArgsConstructor;
import net.smc.dto.ParseQueueDto;
import net.smc.dto.dtofromjson.SteamItemFromJsonDto;
import net.smc.entities.ParseQueue;
import net.smc.entities.SteamItem;
import net.smc.enums.SteamItemType;
import net.smc.readers.ParseQueueReader;
import net.smc.repositories.ParseQueueRepository;
import net.smc.repositories.SteamItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParseQueueService {

    @Value("${queue.max-attempts}")
    private Integer maxQueueAttempts;

    @Value("${default-parse-period.steam-item}")
    private Integer defaultSteamItemParsePeriod;
    private final ParseQueueReader parseQueueReader;
    private final ParseQueueRepository parseQueueRepository;
    private final SteamItemRepository steamItemRepository;
    private final ParserService parserService;

    @Scheduled(fixedDelayString = "${scheduled.parse-queue}", initialDelay = 1000)
    public void parseActiveNamesByPeriod() {
        List<ParseQueue> allActualParseQueues = parseQueueRepository.findAllByArchiveOrderByImportanceDescIdAsc(false);
        if (allActualParseQueues.size() > 0) {
            ParseQueue oneActualQueue = allActualParseQueues.get(0);
            switch (oneActualQueue.getParseType()) {
                case LOT -> {
                }
                case SKIN, STICKER -> {
                    SteamItemFromJsonDto steamItemFromJsonDto = parserService.parseSteamItemFromApi(oneActualQueue.getParseUrl());
                    if (steamItemFromJsonDto != null
                            && steamItemFromJsonDto.getMinPrice() != null
                            && steamItemFromJsonDto.getMedianPrice() != null) {
                        List<SteamItem> steamItemsWithThisName = steamItemRepository.findAllByName(oneActualQueue.getParseTarget());
                        // Если такой скин/стикер не существуют
                        if (steamItemsWithThisName.size() == 0) {
                            // Создаем в базе новый steamItem. Проставляем ему название, цены, тип.
                            SteamItem newSteamItem = new SteamItem(
                                    oneActualQueue.getParseTarget(),
                                    steamItemFromJsonDto.getMinPrice(),
                                    steamItemFromJsonDto.getMedianPrice(),
                                    SteamItemType.valueOf(oneActualQueue.getParseType().toString()),
                                    defaultSteamItemParsePeriod);
                                steamItemRepository.saveAndFlush(newSteamItem);
                        } else if (steamItemsWithThisName.size() == 1) { // Если такой скин/стикер уже существуют.
                            // Обновляем в базе старый steamItem. Проставляем ему цены.
                            steamItemsWithThisName.get(0).updateSteamItemPrices(
                                    steamItemFromJsonDto.getMinPrice(), steamItemFromJsonDto.getMedianPrice());
                            steamItemRepository.saveAllAndFlush(steamItemsWithThisName);
                        } else {
                            // не должно случиться по constraint
                            throw new RuntimeException("Произошло нарушение целостности steamItem-таблицы");
                        }
                        // если всё успешно
                        oneActualQueue.setArchive(true);
                        parseQueueRepository.saveAndFlush(oneActualQueue);
                    } else {
                        oneActualQueue.setAttempt(oneActualQueue.getAttempt() + 1);
                        if (oneActualQueue.getAttempt() > maxQueueAttempts) {
                            parseQueueRepository.delete(oneActualQueue);
                        } else {
                            parseQueueRepository.saveAndFlush(oneActualQueue);
                        }
                    }
                }
            }

        }
    }

    public List<ParseQueueDto> getAllParseQueues(Boolean archive) {
        return parseQueueReader.getAllParseQueues(archive);
    }
}
