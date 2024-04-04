package net.smc.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.smc.dto.DefaultChildDto;

@Entity
@Data
@NoArgsConstructor
public class DefaultChild {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long defaultParentId;
    private String name;
    private boolean archive = false;

    public DefaultChild(Long defaultParentId, String name) {
        this.defaultParentId = defaultParentId;
        this.name = name;
    }

    public DefaultChild(DefaultChildDto defaultChildDto) {
        this.id = defaultChildDto.getId();
        this.defaultParentId = defaultChildDto.getDefaultParentId();
        this.name = defaultChildDto.getName();
        this.archive = defaultChildDto.isArchive();
    }

    public void update(DefaultChildDto defaultChildDto) {
        this.defaultParentId = defaultChildDto.getDefaultParentId();
        this.name = defaultChildDto.getName();
        this.archive = defaultChildDto.isArchive();
    }

    public void archive() {
        this.archive = true;
    }
}
