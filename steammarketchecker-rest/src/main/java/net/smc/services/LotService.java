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
                                        .mapToDouble(e -> e).sum() / 10)
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
}
