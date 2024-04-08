package net.smc.dto.dtofromjson;

import com.google.gson.JsonElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class LotFromJsonDto {
    private Long listingId;
    private Double price; // цена - видимо, указывается автором в его валюте
    private Double fee; // комиссия
    private Double convertedPrice; // цена - нормированная
    private Double convertedFee; // комиссия - нормированная
    private Long assetId;
    private String stickersAsString;

    public LotFromJsonDto(JsonElement jsonElementListingInfo) {
        Map<String, JsonElement> mapListingInfo = jsonElementListingInfo.getAsJsonObject().asMap();
        this.listingId = mapListingInfo.get("listingid").getAsLong();
        this.price = mapListingInfo.get("price").getAsDouble();
        this.fee = mapListingInfo.get("fee").getAsDouble();
        this.convertedPrice = mapListingInfo.get("converted_price").getAsDouble();
        this.convertedFee = mapListingInfo.get("converted_fee").getAsDouble();
        this.assetId = mapListingInfo.get("asset").getAsJsonObject().asMap().get("id").getAsLong();
    }
}