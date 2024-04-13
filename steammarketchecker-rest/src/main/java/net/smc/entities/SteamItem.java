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
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name"}) })
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
    private Integer parsePeriod; // in sec
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
        this.parseQueueId = steamItemDto.getParseQueueId(); //todo появились вопросы к этой штуке - она вроде не нужна
        this.parseDate = steamItemDto.getParseDate();
        this.parsePeriod = steamItemDto.getParsePeriod();
        this.forceUpdate = steamItemDto.isForceUpdate();
        this.steamItemType = steamItemDto.getSteamItemType();
    }

    public SteamItem(String name, Double minPrice, Double medianPrice, SteamItemType steamItemType, Integer parsePeriod) {
        this.name = name;
        this.minPrice = minPrice;
        this.medianPrice = medianPrice;
        this.parseDate = Instant.now();
        this.parsePeriod = parsePeriod;
        this.steamItemType = steamItemType;
    }

    public void updateSteamItemPrices(Double minPrice, Double medianPrice) {
        this.setMinPrice(minPrice);
        this.setMedianPrice(medianPrice);
        this.setParseDate(Instant.now());
        this.setParseQueueId(null);
    }

    public void processOutdatedSteamItem() {
        this.setParseDate(Instant.now());
        this.setForceUpdate(false);
    }

    public void forceUpdate() {
        this.forceUpdate = true;
    }
}
