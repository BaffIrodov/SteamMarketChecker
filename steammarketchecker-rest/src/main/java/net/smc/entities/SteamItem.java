package net.smc.entities;

import jakarta.persistence.MappedSuperclass;

import java.time.Instant;

// таблица-справочник для скинов
@MappedSuperclass
public class SteamItem {
    private Long steamItemId;
    private String name;
    private Double minPrice;
    private Double medianPrice;
    private Long parseQueueId;
    private Instant parseDate;
}
