package net.smc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.smc.entities.ActiveName;
import net.smc.entities.DefaultChild;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveNameDto {
    private Long id;
    private String itemName;
    private Integer parseItemCount;
    private Integer parsePeriod; // in sec
    private Instant lastParseDate;
    private boolean archive;
    private boolean forceUpdate;

    public ActiveNameDto(ActiveName activeName) {
        this.id = activeName.getId();
        this.itemName = activeName.getItemName();
        this.parseItemCount = activeName.getParseItemCount();
        this.parsePeriod = activeName.getParsePeriod();
        this.lastParseDate = activeName.getLastParseDate();
        this.archive = activeName.isArchive();
        this.forceUpdate = activeName.isForceUpdate();
    }
}
