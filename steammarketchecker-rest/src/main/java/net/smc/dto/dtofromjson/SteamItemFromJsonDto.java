package net.smc.dto.dtofromjson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class SteamItemFromJsonDto {
    private Double minPrice;
    private Double medianPrice;

    public SteamItemFromJsonDto(JsonObject jsonObject) {
        Map<String, JsonElement> mapSteamItemInfo = jsonObject.asMap();
        this.minPrice = Double.parseDouble(getDoubleParseReadyString(mapSteamItemInfo.get("lowest_price").getAsString()));
        this.medianPrice = Double.parseDouble(getDoubleParseReadyString(mapSteamItemInfo.get("median_price").getAsString()));
    }

    private String getDoubleParseReadyString(String string) {
        return string.replaceAll(" .*", "").replaceAll(",", ".");
    }
}
