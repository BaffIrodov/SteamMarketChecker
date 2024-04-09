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
public class ActiveName {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String itemName;
    private Integer parsePeriod; // in sec
    private Instant lastParseDate;
    private boolean archive;

    public ActiveName(ActiveNameDto activeNameDto) {
        this.id = activeNameDto.getId();
        this.itemName = activeNameDto.getItemName();
        this.parsePeriod = activeNameDto.getParsePeriod();
        this.lastParseDate = activeNameDto.getLastParseDate();
        this.archive = activeNameDto.isArchive();
    }

    public void update(ActiveNameDto activeNameDto) {
        this.itemName = activeNameDto.getItemName();
        this.parsePeriod = activeNameDto.getParsePeriod();
        this.lastParseDate = activeNameDto.getLastParseDate();
        this.archive = activeNameDto.isArchive();
    }

    public void archive() {
        this.archive = true;
    }

}
