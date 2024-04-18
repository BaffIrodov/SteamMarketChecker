package net.smc.entities;

import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.smc.common.CommonUtils;
import net.smc.enums.ParseType;

import java.util.Arrays;

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
    private Long steamItemId; // Для лота - это id скина в базе
    private boolean archive;
    private Integer attempt = 0;

    // конструктор на основе дто не нужен - никогда не создаем сущность от дто

    public ParseQueue(Integer importance, ParseType parseType, String parseTarget, String parseUrl) {
        this.importance = importance;
        this.parseType = parseType;
        this.parseTarget = parseTarget;
        this.parseUrl = parseUrl;
    }

    public ParseQueue(Integer importance, ParseType parseType, String parseTarget,
                      Integer parseItemCount, Long steamItemId, CommonUtils commonUtils) {
        this.importance = importance;
        this.parseType = parseType;
        this.parseTarget = parseTarget;
        switch (parseType) {
            case LOT -> {
                this.steamItemId = steamItemId;
                String convertedLotName = commonUtils.defaultStringConverter(parseTarget.replaceAll("_lot", ""));
                this.parseUrl = String.format("https://steamcommunity.com/market/listings/730/%s/render/?query=&count=%d&country=UK&language=english&currency=5",
                        convertedLotName, parseItemCount);
            }
            case SKIN -> {
                String convertedItemName = commonUtils.defaultStringConverter(parseTarget.replaceAll("_skin", ""));
                this.parseUrl = String.format("https://steamcommunity.com/market/priceoverview/?appid=730&currency=5&market_hash_name=%s",
                        convertedItemName);
            }
            case STICKER -> {
                String convertedItemName = commonUtils.defaultStringConverter("Sticker | " + parseTarget.replaceAll("_sticker", ""));
                this.importance = calculateImportanceForSticker(convertedItemName);
                this.parseUrl = String.format("https://steamcommunity.com/market/priceoverview/?appid=730&currency=5&market_hash_name=%s",
                        convertedItemName);
            }
        }
    }

    // Важные стикеры лучше считать сразу же
    private int calculateImportanceForSticker(String stickerName) {
        int importance = 0;
        if (stickerName.contains("Holo")) importance += 1;
        if (stickerName.contains("Foil")) importance += 1;
        if (stickerName.contains("Gold")) importance += 2;
        if (Arrays.stream(new String[]{"2013", "2014", "2015"}).anyMatch(stickerName::contains)) importance += 3;
        if (Arrays.stream(new String[]{"2016", "2017", "2018"}).anyMatch(stickerName::contains)) importance += 2;
        if (Arrays.stream(new String[]{"2019", "2020", "2021", "2022", "2023", "2024"}).anyMatch(stickerName::contains)) importance += 1;
        return importance;
    }
}
