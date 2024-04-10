package net.smc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.smc.entities.SteamItem;
import net.smc.enums.SteamItemType;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SteamItemDto {
    private Long id;
    private Long steamItemId;
    private String name;
    private Double minPrice;
    private Double medianPrice;
    private Long parseQueueId;
    private Instant parseDate;
    private boolean forceUpdate;
    private SteamItemType steamItemType;

    public SteamItemDto(SteamItem steamItem) {
        this.id = steamItem.getId();
        this.steamItemId = steamItem.getSteamItemId();
        this.name = steamItem.getName();
        this.minPrice = steamItem.getMinPrice();
        this.medianPrice = steamItem.getMedianPrice();
        this.parseQueueId = steamItem.getParseQueueId();
        this.parseDate = steamItem.getParseDate();
        this.forceUpdate = steamItem.isForceUpdate();
        this.steamItemType = steamItem.getSteamItemType();
    }
}
