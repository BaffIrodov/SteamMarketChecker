package net.smc.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
