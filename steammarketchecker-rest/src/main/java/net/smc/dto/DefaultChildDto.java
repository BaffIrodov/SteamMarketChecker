package net.smc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.smc.entities.DefaultChild;

@Data
@NoArgsConstructor
public class DefaultChildDto {
    private Long id;
    private Long defaultParentId;
    private String name;
    private boolean archive;

    public DefaultChildDto(DefaultChild defaultChild) {
        this.id = defaultChild.getId();
        this.defaultParentId = defaultChild.getDefaultParentId();
        this.name = defaultChild.getName();
        this.archive = defaultChild.isArchive();
    }
}
