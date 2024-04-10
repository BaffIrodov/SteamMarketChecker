package net.smc.entities;

import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.smc.enums.ParseType;

// таблица очереди парсинга
@Entity
@Data
@NoArgsConstructor
public class ParseQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Integer importance = 0;

    @Enumerated(EnumType.STRING)
    private ParseType parseType;

    private String parseTarget;
    private String parseUrl;
    private boolean archive;

    // конструктор на основе дто не нужен - никогда не создаем сущность от дто

    public ParseQueue(Integer importance, ParseType parseType, String parseTarget, String parseUrl) {
        this.importance = importance;
        this.parseType = parseType;
        this.parseTarget = parseTarget;
        this.parseUrl = parseUrl;
    }

}
