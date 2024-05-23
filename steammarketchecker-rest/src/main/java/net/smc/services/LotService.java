package net.smc.services;

import lombok.RequiredArgsConstructor;
import net.smc.dto.ActualCurrencyRelationDto;
import net.smc.dto.LotDto;
import net.smc.dto.SteamItemDto;
import net.smc.entities.ActualCurrencyRelation;
import net.smc.entities.Lot;
import net.smc.readers.ActualCurrencyRelationReader;
import net.smc.readers.LotReader;
import net.smc.repositories.ActualCurrencyRelationRepository;
import net.smc.repositories.LotRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LotService {
    private final LotReader lotReader;
    private final ActualCurrencyRelationReader actualCurrencyRelationReader;
    private final LotRepository lotRepository;
    private final ActualCurrencyRelationRepository actualCurrencyRelationRepository;
    private final TelegramBotService telegramBotService;

    public List<LotDto> getAllLots(Boolean onlyActual, Boolean onlyCompleteness, Boolean onlyProfitability) {
        // todo надо маппиться нормально - пока и так сойдет
        // todo либо можно не получать всё сразу, а только по открытию диалога делать такой запрос
        List<LotDto> allLots = lotReader.getAllLots(onlyActual, onlyCompleteness, onlyProfitability);
        Map<Long, LotDto> mapLotDtoById = allLots.stream().collect(Collectors.toMap(LotDto::getId, e -> e));
        List<Lot> lots = lotRepository.findAllById(allLots.stream().map(LotDto::getId).toList());
        lots.forEach(lotEntity -> {
            List<SteamItemDto> stickerDtoList = new ArrayList<>();
            lotEntity.getLotStickerList().forEach(lotSticker -> {
                stickerDtoList.add(new SteamItemDto(lotSticker.getSteamSticker()));
            });
            mapLotDtoById.get(lotEntity.getId()).setStickers(stickerDtoList);
        });
        return allLots;
    }

    public ActualCurrencyRelationDto getActualCurrencyRelation() {
        return actualCurrencyRelationReader.getActualCurrencyRelationDto();
    }

    @Scheduled(fixedDelayString = "${scheduled.lot-profit}", initialDelay = 1000)
    public void calculateLotProfit() {
        List<ActualCurrencyRelation> actualCurrencyRelations = actualCurrencyRelationRepository.findAllByArchive(false);
        if (actualCurrencyRelations != null && actualCurrencyRelations.size() == 1) {
            ActualCurrencyRelation currencyRelation = actualCurrencyRelations.get(0);
            List<Lot> allLots = lotRepository.findAll();
            for (Lot lot : allLots) {
                // Если профит ещё не считался
                if (!lot.isCompleteness()) {
                    if (lot.getSteamItem().getMinPrice() != null
                            && lot.getLotStickerList().stream().map(e ->
                            e.getSteamSticker().getMinPrice()).allMatch(Objects::nonNull)) {
                        lot.setCompleteness(true);
                        lot.setActual(true);
                        lot.setRealPrice(
                                lot.getSteamItem().getMinPrice() * currencyRelation.getRelation()
                                        + (lot.getLotStickerList().stream().map(e -> e.getSteamSticker().getMinPrice() * currencyRelation.getRelation())
                                        .mapToDouble(e -> e).sum() / 17)
                        );
                        lot.setProfit(((lot.getRealPrice() * 0.85) - lot.getConvertedPrice()));
                        lot.setProfitability(lot.getProfit() > 0);
                        lot.setPriceCalculatingDate(Instant.now());
                    }
                }
            }
            lotRepository.saveAllAndFlush(allLots);
        }
    }

    @Scheduled(fixedDelayString = "${scheduled.telegram-bot}", initialDelay = 1000)
    public void sendTelegramMessageToBaff() {
        String messageToBaff = "";
        List<ActualCurrencyRelation> actualCurrencyRelations = actualCurrencyRelationRepository.findAllByArchive(false);
        List<Lot> allLots = lotRepository.findAll();
        List<Lot> lotsWithProfit = allLots.stream().filter(lot -> lot.isCompleteness() && lot.isActual() && lot.isProfitability()).toList();
        if (lotsWithProfit.size() > 0) {
            boolean neededToSend = false;
            messageToBaff += "Есть выгодные предложения:\n";
            if (actualCurrencyRelations.size() == 1) messageToBaff += "(Цена в рублях)\n";
            int index = 0;
            for (Lot lot : lotsWithProfit) {
                LotDto lotDto = new LotDto(lot);
                if (actualCurrencyRelations.size() == 1) {
                    Double currencyRelation = actualCurrencyRelations.get(0).getRelation();
                    lotDto.setProfit(lotDto.getProfit() / currencyRelation);
                    lotDto.setConvertedPrice(lotDto.getConvertedPrice() / currencyRelation);
                    lotDto.setRealPrice(lotDto.getRealPrice() / currencyRelation);
                }
                if (Math.round(lotDto.getProfit()) > 100
                        && Math.round((lotDto.getProfit() / lotDto.getConvertedPrice() * 100)) > 10) {
                    neededToSend = true;
                    index++;
                    messageToBaff += String.format("\nСкин: %s; \n" +
                                    "Позиция в листинге: %s; \n" +
                                    "Стикеры: %s; \n" +
                                    "Профит: %s; \n" +
                                    "Профит в процентах: %s; \n" +
                                    "Изначальная цена: %s;\n",
                            lotDto.getLotParseTarget(),
                            lotDto.getPositionInListing(),
                            lotDto.getStickersAsString(),
                            Math.round(lotDto.getProfit()),
                            Math.round((lotDto.getProfit() / lotDto.getConvertedPrice() * 100)),
                            Math.round(lotDto.getConvertedPrice())
                    );
                    if (index > 4) {
                        telegramBotService.sendMsg(messageToBaff);
                        messageToBaff = "";
                        index = 0;
                    }
                }
            }
            if (neededToSend) {
                telegramBotService.sendMsg(messageToBaff);
                telegramBotService.sendMsg("-------------------------- Конец передачи ---------------------------");
            }
        }
    }
}
