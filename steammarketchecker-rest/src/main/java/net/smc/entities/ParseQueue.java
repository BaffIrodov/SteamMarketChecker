package net.smc.entities;

import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.smc.common.CommonUtils;
import net.smc.enums.ParseType;

// таблица очереди парсинга
@Entity
@Data
@NoArgsConstructor
public class ParseQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Integer importance = 0;

    @Enumerated(EnumType.STRING)
    private ParseType parseType;

    private String parseTarget;
    private String parseUrl;
    private boolean archive;

    // конструктор на основе дто не нужен - никогда не создаем сущность от дто

    public ParseQueue(Integer importance, ParseType parseType, String parseTarget, String parseUrl) {
        this.importance = importance;
        this.parseType = parseType;
        this.parseTarget = parseTarget;
        this.parseUrl = parseUrl;
    }

    public ParseQueue(Integer importance, ParseType parseType, String parseTarget,
                      Integer parseItemCount, CommonUtils commonUtils) {
        this.importance = importance;
        this.parseType = parseType;
        this.parseTarget = parseTarget;
        switch (parseType) {
            case LOT -> {
                String convertedLotName = commonUtils.defaultStringConverter(parseTarget);
                this.parseUrl = String.format("https://steamcommunity.com/market/listings/730/%s/render/?query=&count=%d&country=UK&language=english&currency=5",
                        convertedLotName, parseItemCount);
            }
            case SKIN, STICKER -> {
                String convertedItemName = commonUtils.defaultStringConverter(parseTarget);
                this.parseUrl = String.format("https://steamcommunity.com/market/priceoverview/?appid=730&currency=5&market_hash_name=%s",
                        convertedItemName);
            }
        }
    }

}
