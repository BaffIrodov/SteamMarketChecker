package net.smc.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.smc.common.CommonUtils;
import net.smc.dto.dtofromjson.LotFromJsonDto;
import net.smc.dto.dtofromjson.SteamItemFromJsonDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ParserService {
    private final CommonUtils commonUtils;

//    @PostConstruct
    public void getListing() {
        int countAsInt = 10;
        String itemName = "StatTrak™ AWP | Fever Dream (Factory New)";
        String convertedItemName = commonUtils.defaultStringConverter(itemName);
        String url = String.format("https://steamcommunity.com/market/listings/730/%s/render/?query=&count=%d&country=UK&language=english&currency=5",
                convertedItemName, countAsInt);

        String listingJsonAsString = commonUtils.connectAndGetJsonAsString(url);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject listingJson = gson.fromJson(listingJsonAsString, JsonObject.class);
        List<LotFromJsonDto> lotsWithStickers = new ArrayList<>();
        if (listingJson != null && listingJson.asMap().get("success").getAsBoolean()) {
            Map<String, JsonElement> map = listingJson.asMap().get("listinginfo").getAsJsonObject().asMap();
            map.values().forEach(jsonElement -> {
                LotFromJsonDto lotFromJsonDto = new LotFromJsonDto(jsonElement);
                String assetDescriptions = listingJson.asMap().get("assets").getAsJsonObject().asMap()
                        .get("730").getAsJsonObject().asMap().get("2").getAsJsonObject().asMap()
                        .get(lotFromJsonDto.getAssetId().toString()).getAsJsonObject().asMap().get("descriptions").toString();
                if (assetDescriptions.contains("Sticker")) {
                    String stickersAsString = assetDescriptions.replaceAll(".*(?=.*Sticker).{9}", "")
                            .replaceAll("</center>.*", "");
                    lotFromJsonDto.setStickersAsString(stickersAsString);
                    lotsWithStickers.add(lotFromJsonDto);
                }
            });
        }
    }

//    @PostConstruct
    public void getSteamItem() {
        String itemName = "StatTrak™ Five-SeveN | Nightshade (Minimal Wear)";
        String convertedItemName = commonUtils.defaultStringConverter(itemName);

        String url = String.format("https://steamcommunity.com/market/priceoverview/?appid=730&currency=5&market_hash_name=%s",
                convertedItemName);

        String priceOverviewJsonAsString = commonUtils.connectAndGetJsonAsString(url);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject priceOverviewJson = gson.fromJson(priceOverviewJsonAsString, JsonObject.class);
        if (priceOverviewJson != null && priceOverviewJson.asMap().get("success").getAsBoolean()) {
            Map<String, JsonElement> map = priceOverviewJson.asMap();
            SteamItemFromJsonDto steamItemFromJsonDto = new SteamItemFromJsonDto(priceOverviewJson);
        }
    }

}
