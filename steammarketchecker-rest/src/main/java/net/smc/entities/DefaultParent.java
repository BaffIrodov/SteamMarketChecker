package net.smc.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.smc.dto.DefaultParentDto;

@Entity
@Data
@NoArgsConstructor
public class DefaultParent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private boolean archive = false;

    public DefaultParent(String name) {
        this.name = name;
    }

    public DefaultParent(DefaultParentDto defaultParentDto) {
        this.id = defaultParentDto.getId();
        this.name = defaultParentDto.getName();
        this.archive = defaultParentDto.isArchive();
    }

    public void update(DefaultParentDto defaultParentDto) {
        this.name = defaultParentDto.getName();
        this.archive = defaultParentDto.isArchive();
    }

    public void archive() {
        this.archive = true;
    }
}
