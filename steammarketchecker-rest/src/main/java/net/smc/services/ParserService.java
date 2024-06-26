package net.smc.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.smc.common.CommonUtils;
import net.smc.dto.dtofromjson.LotFromJsonDto;
import net.smc.dto.dtofromjson.ParseResultForLot;
import net.smc.dto.dtofromjson.ParseResultForSteamItem;
import net.smc.dto.dtofromjson.SteamItemFromJsonDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Log4j2
public class ParserService {
    private final CommonUtils commonUtils;

    public ParseResultForLot parseListingFromApi(String url) {
        String listingJsonAsString = commonUtils.connectAndGetJsonAsString(url);
        AtomicInteger positionInListing = new AtomicInteger();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject listingJson = gson.fromJson(listingJsonAsString, JsonObject.class);
        ParseResultForLot parseResultForLot = new ParseResultForLot();
        List<LotFromJsonDto> lotsWithStickers = new ArrayList<>();
        if (listingJson != null && listingJson.asMap().get("success").getAsBoolean()
                && listingJson.asMap().get("listinginfo").isJsonObject()) {
            Map<String, JsonElement> map = listingJson.asMap().get("listinginfo").getAsJsonObject().asMap();
            map.values().forEach(jsonElement -> {
                if (jsonElement.getAsJsonObject().asMap().get("listingid") != null //может забаговаться одна цена какая-нибудь, тогда весь лот отлетит с ошибкой
                        && jsonElement.getAsJsonObject().asMap().get("price") != null
                        && jsonElement.getAsJsonObject().asMap().get("fee") != null
                        && jsonElement.getAsJsonObject().asMap().get("converted_price") != null
                        && jsonElement.getAsJsonObject().asMap().get("converted_fee") != null
                        && jsonElement.getAsJsonObject().asMap().get("asset") != null
                ) {
                    LotFromJsonDto lotFromJsonDto = new LotFromJsonDto(jsonElement);
                    String assetDescriptions = listingJson.asMap().get("assets").getAsJsonObject().asMap()
                            .get("730").getAsJsonObject().asMap().get("2").getAsJsonObject().asMap()
                            .get(lotFromJsonDto.getAssetId().toString()).getAsJsonObject().asMap().get("descriptions").toString();
                    if (assetDescriptions.contains("Sticker")) {
                        String stickersAsString = assetDescriptions.replaceAll(".*(?=.*Sticker).{9}", "")
                                .replaceAll("</center>.*", "");
                        lotFromJsonDto.setStickersAsString(stickersAsString);
                        lotFromJsonDto.setPositionInListing(positionInListing.get());
                        lotsWithStickers.add(lotFromJsonDto);
                    }
                }
                positionInListing.getAndIncrement();
            });
            parseResultForLot.setConnectSuccessful(true);
            parseResultForLot.setLotWithStickersFromJsonDtoList(lotsWithStickers);
        } else {
            log.error("Не удалось распарсить lot по юрлу: " + url);
        }
        return parseResultForLot;
    }

    public ParseResultForSteamItem parseSteamItemFromApi(String url) {
        ParseResultForSteamItem parseResultForSteamItem = new ParseResultForSteamItem();
        SteamItemFromJsonDto steamItemFromJsonDto = null;
        String priceOverviewJsonAsString = commonUtils.connectAndGetJsonAsString(url);
        if (priceOverviewJsonAsString.equals("")) { //todo - посмотреть при эксплуатации, можно ли убрать
            // Произошла ошибка парсинга или такого steamItem не существует
            System.out.println("(Пришла пустая строка) Произошла ошибка парсинга или такого steamItem не существует; Для url: " + url);
            steamItemFromJsonDto = new SteamItemFromJsonDto();
            steamItemFromJsonDto.setMinPrice(-1d);
            parseResultForSteamItem.setConnectSuccessful(true);
            parseResultForSteamItem.setSteamItemFromJsonDto(steamItemFromJsonDto);
            return parseResultForSteamItem;
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject priceOverviewJson = gson.fromJson(priceOverviewJsonAsString, JsonObject.class);
        if (priceOverviewJson != null && priceOverviewJson.asMap().get("success").getAsBoolean()
                && priceOverviewJson.asMap().get("lowest_price") != null) {
            steamItemFromJsonDto = new SteamItemFromJsonDto(priceOverviewJson);
            parseResultForSteamItem.setConnectSuccessful(true);
            parseResultForSteamItem.setSteamItemFromJsonDto(steamItemFromJsonDto);
        } else if (priceOverviewJson != null && priceOverviewJson.toString().equals("{\"success\":true}"))  {
            // SteamItem существует, но крайне дорогой, поэтому его нет на ТП
            System.out.println("SteamItem существует, но крайне дорогой, поэтому его нет на ТП; Для url: " + url);
            steamItemFromJsonDto = new SteamItemFromJsonDto();
            steamItemFromJsonDto.setMinPrice(0d);
            parseResultForSteamItem.setConnectSuccessful(true);
            parseResultForSteamItem.setSteamItemFromJsonDto(steamItemFromJsonDto);
        } else if (priceOverviewJson != null && priceOverviewJson.toString().equals("{\"success\":false}")) {
            // Произошла ошибка парсинга или такого steamItem не существует
            System.out.println("Произошла ошибка парсинга или такого steamItem не существует; Для url: " + url);
            steamItemFromJsonDto = new SteamItemFromJsonDto();
            steamItemFromJsonDto.setMinPrice(-1d);
            parseResultForSteamItem.setConnectSuccessful(true);
            parseResultForSteamItem.setSteamItemFromJsonDto(steamItemFromJsonDto);
        }
        else {
            log.error("Не удалось распарсить steamItem по юрлу: " + url);
        }
        return parseResultForSteamItem;
    }

}
