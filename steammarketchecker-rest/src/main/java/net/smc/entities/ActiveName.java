package net.smc.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.smc.dto.ActiveNameDto;
import net.smc.dto.DefaultChildDto;

import java.time.Instant;

// таблица со списком itemName для парсинга
@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "itemName"}) })
public class ActiveName {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String itemName;
    private Integer parseItemCount;
    private Integer parsePeriod; // in sec
    private Instant lastParseDate;
    private boolean archive;
    private boolean forceUpdate;

    public ActiveName(ActiveNameDto activeNameDto) {
        this.id = activeNameDto.getId();
        this.itemName = activeNameDto.getItemName();
        this.parseItemCount = activeNameDto.getParseItemCount();
        this.parsePeriod = activeNameDto.getParsePeriod();
        this.lastParseDate = activeNameDto.getLastParseDate();
        this.archive = activeNameDto.isArchive();
        this.forceUpdate = activeNameDto.isForceUpdate();
    }

    public ActiveName(String itemName, Integer parseItemCount, Integer parsePeriod) {
        this.itemName = itemName;
        this.parseItemCount = parseItemCount;
        this.parsePeriod = parsePeriod;
    }

    public void update(ActiveNameDto activeNameDto) {
        this.itemName = activeNameDto.getItemName();
        this.parseItemCount = activeNameDto.getParseItemCount();
        this.parsePeriod = activeNameDto.getParsePeriod();
        this.lastParseDate = activeNameDto.getLastParseDate();
        this.archive = activeNameDto.isArchive();
        this.forceUpdate = activeNameDto.isForceUpdate();
    }

    public void archiveOrUnarchive() {
        this.archive = !this.archive;
    }

    public void forceUpdate() {
        this.forceUpdate = true;
    }

}
