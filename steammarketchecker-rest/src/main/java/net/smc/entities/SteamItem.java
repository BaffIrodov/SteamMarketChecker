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
    @Enumerated(EnumType.STRING)
    @DefaultValue("SKIN")
    private SteamItemType steamItemType;
    private String name;
    private Double minPrice;
    private Long parseQueueId;
    private Instant parseDate;
    private Integer parsePeriod; // in sec
    private boolean forceUpdate;

    public SteamItem(SteamItemDto steamItemDto) {
        this.id = steamItemDto.getId();
        this.name = steamItemDto.getName();
        this.minPrice = steamItemDto.getMinPrice();
        this.parseQueueId = steamItemDto.getParseQueueId(); //todo появились вопросы к этой штуке - она вроде не нужна
        this.parseDate = steamItemDto.getParseDate();
        this.parsePeriod = steamItemDto.getParsePeriod();
        this.forceUpdate = steamItemDto.isForceUpdate();
        this.steamItemType = steamItemDto.getSteamItemType();
    }

    public SteamItem(String name, Double minPrice, SteamItemType steamItemType, Integer parsePeriod) {
        this.name = name;
        this.minPrice = minPrice;
        this.parseDate = Instant.now();
        this.steamItemType = steamItemType;
        this.parsePeriod = parsePeriod;
    }

    // На случай, когда нужно создать пустышку-заглушку для связи лота и скина/стикера
    public SteamItem(String name, SteamItemType steamItemType, Integer parsePeriod) {
        this.name = name;
        this.steamItemType = steamItemType;
        this.parsePeriod = parsePeriod;
    }

    public void updateSteamItemPrices(Double minPrice) {
        this.setMinPrice(minPrice);
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
