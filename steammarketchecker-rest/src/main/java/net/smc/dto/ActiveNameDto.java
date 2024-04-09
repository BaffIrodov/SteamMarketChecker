package net.smc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.smc.entities.ActiveName;
import net.smc.entities.DefaultChild;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ActiveNameDto {
    private Long id;
    private String itemName;
    private Integer parsePeriod; // in sec
    private Instant lastParseDate;
    private boolean archive;

    public ActiveNameDto(ActiveName activeName) {
        this.id = activeName.getId();
        this.itemName = activeName.getItemName();
        this.parsePeriod = activeName.getParsePeriod();
        this.lastParseDate = activeName.getLastParseDate();
        this.archive = activeName.isArchive();
    }
}
