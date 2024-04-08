package net.smc.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.smc.enums.ParseTypeEnum;

// таблица очереди парсинга
@Entity
@Data
@NoArgsConstructor
public class ParseQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Integer importance;

    @Enumerated(EnumType.STRING)
    private ParseTypeEnum parseType;

    private String parseTarget;
    private String parseUrl;

}
