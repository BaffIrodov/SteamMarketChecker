package net.smc.entities;

import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.smc.dto.SteamItemDto;
import net.smc.enums.SteamItemType;

import java.time.Instant;

// таблица-справочник для скинов
@Entity
@Data
@NoArgsConstructor
public class SteamItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long steamItemId;
    private String name;
    private Double minPrice;
    private Double medianPrice;
    private Long parseQueueId;
    private Instant parseDate;
    private boolean forceUpdate;
    @Enumerated(EnumType.STRING)
    @DefaultValue("SKIN")
    private SteamItemType steamItemType;

    public SteamItem(SteamItemDto steamItemDto) {
        this.id = steamItemDto.getId();
        this.steamItemId = steamItemDto.getSteamItemId();
        this.name = steamItemDto.getName();
        this.minPrice = steamItemDto.getMinPrice();
        this.medianPrice = steamItemDto.getMedianPrice();
        this.parseQueueId = steamItemDto.getParseQueueId();
        this.parseDate = steamItemDto.getParseDate();
        this.forceUpdate = steamItemDto.isForceUpdate();
        this.steamItemType = steamItemDto.getSteamItemType();
    }

    public void forceUpdate() {
        this.forceUpdate = true;
    }
}
