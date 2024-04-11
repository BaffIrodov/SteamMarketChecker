package net.smc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.smc.entities.ParseQueue;
import net.smc.enums.ParseType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParseQueueDto {
    private Long id;
    private Integer importance;
    private ParseType parseType;
    private String parseTarget;
    private String parseUrl;
    private boolean archive;
    private Integer attempt;

    public ParseQueueDto(ParseQueue parseQueue) {
        this.id = parseQueue.getId();
        this.importance = parseQueue.getImportance();
        this.parseType = parseQueue.getParseType();
        this.parseTarget = parseQueue.getParseTarget();
        this.parseUrl = parseQueue.getParseUrl();
        this.archive = parseQueue.isArchive();
        this.attempt = parseQueue.getAttempt();
    }
}
