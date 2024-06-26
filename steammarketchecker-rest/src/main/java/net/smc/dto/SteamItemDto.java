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
    private String name;
    private Double minPrice;
    private Long parseQueueId;
    private Instant parseDate;
    private Integer parsePeriod; // in sec
    private boolean forceUpdate;
    private SteamItemType steamItemType;

    public SteamItemDto(SteamItem steamItem) {
        this.id = steamItem.getId();
        this.name = steamItem.getName();
        this.minPrice = steamItem.getMinPrice();
        this.parseQueueId = steamItem.getParseQueueId();
        this.parseDate = steamItem.getParseDate();
        this.parsePeriod = steamItem.getParsePeriod();
        this.forceUpdate = steamItem.isForceUpdate();
        this.steamItemType = steamItem.getSteamItemType();
    }
}
