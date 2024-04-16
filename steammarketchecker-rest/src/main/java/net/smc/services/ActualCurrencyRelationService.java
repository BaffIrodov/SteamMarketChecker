package net.smc.services;

import lombok.RequiredArgsConstructor;
import net.smc.common.CommonUtils;
import net.smc.dto.dtofromjson.ParseResultForLot;
import net.smc.dto.dtofromjson.ParseResultForSteamItem;
import net.smc.entities.ActualCurrencyRelation;
import net.smc.readers.ParseQueueReader;
import net.smc.readers.SteamItemReader;
import net.smc.repositories.ActualCurrencyRelationRepository;
import net.smc.repositories.LotRepository;
import net.smc.repositories.ParseQueueRepository;
import net.smc.repositories.SteamItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActualCurrencyRelationService {
    private final List<String> itemNamesForPING = List.of(
            "AWP | Electric Hive (Factory New)",
            "Desert Eagle | Oxide Blaze (Factory New)",
            "AWP | Fever Dream (Factory New)",
            "AWP | Electric Hive (Well-Worn)",
            "M4A4 | X-Ray (Minimal Wear)",
            "AWP | Corticera (Factory New)",
            "AK-47 | Blue Laminate (Minimal Wear)",
            "AWP | Electric Hive (Field-Tested)"
    );

    private final SteamItemReader steamItemReader;
    private final SteamItemRepository steamItemRepository;
    private final ActualCurrencyRelationRepository actualCurrencyRelationRepository;
    private final LotRepository lotRepository;
    private final ParseQueueRepository parseQueueRepository;
    private final ParseQueueReader parseQueueReader;
    private final ParserService parserService;
    private final CommonUtils commonUtils;

    @Value("${scheduled.parse-queue}")
    private Integer parseScheduleDuration;

//    @Scheduled(fixedDelayString = "${scheduled.actual-currency-relation}", initialDelay = 1000)
    public void parseActualCurrencyRelationByPeriod() {
        List<Double> convertedPrices = new ArrayList<>();
        List<Double> minPrices = new ArrayList<>();
        List<Double> relations = new ArrayList<>();
        // Пробегаемся по списку скинов, ищем для них цены из listing (converted) и цены из overview (currency)
        // Затем усредняемся и принимаем получившуюся цену за правду
        for (String itemName : itemNamesForPING) {
            String convertedItemName = commonUtils.defaultStringConverter(itemName);
            String parseUrlForSkin = String.format("https://steamcommunity.com/market/priceoverview/?appid=730&currency=5&market_hash_name=%s",
                    convertedItemName);
            ParseResultForSteamItem parseResultForSteamItem = parserService.parseSteamItemFromApi(parseUrlForSkin);
            if (parseResultForSteamItem.isConnectSuccessful()) {
                commonUtils.waiter(parseScheduleDuration);
                String convertedLotName = commonUtils.defaultStringConverter(itemName);
                String parseUrlForLot = String.format("https://steamcommunity.com/market/listings/730/%s/render/?query=&count=%d&country=UK&language=english&currency=5",
                        convertedLotName, 10);
                ParseResultForLot parseResultForLot = parserService.parseListingFromApi(parseUrlForLot);
                if (parseResultForLot.isConnectSuccessful() && parseResultForLot.getLotWithStickersFromJsonDtoList().size() > 0) {
                    convertedPrices.add(parseResultForLot.getLotWithStickersFromJsonDtoList().get(0).getConvertedPrice());
                    minPrices.add(parseResultForSteamItem.getSteamItemFromJsonDto().getMinPrice());
                }
            }
            commonUtils.waiter(parseScheduleDuration);
        }
        for (int i = 0; i < minPrices.size(); i++) {
            relations.add(convertedPrices.get(i) / minPrices.get(i));
        }
        ActualCurrencyRelation actualCurrencyRelation = new ActualCurrencyRelation(
                convertedPrices.stream().mapToDouble(c -> c).average().orElse(0),
                minPrices.stream().mapToDouble(m -> m).average().orElse(0),
                relations.stream().mapToDouble(a -> a).average().orElse(0));
        // Если всё прошло хорошо, данные об отношениях есть, то запомним их в таблицу
        if (actualCurrencyRelation.getConvertedPrice() != 0
                && actualCurrencyRelation.getCurrencyPrice() != 0
                && actualCurrencyRelation.getRelation() != 0) {
            List<ActualCurrencyRelation> actualRelations = actualCurrencyRelationRepository.findAllByArchive(false);
            actualRelations.forEach(e -> e.setArchive(true));
            actualCurrencyRelationRepository.saveAllAndFlush(actualRelations);
            actualCurrencyRelationRepository.saveAndFlush(actualCurrencyRelation);
        }
    }
}
