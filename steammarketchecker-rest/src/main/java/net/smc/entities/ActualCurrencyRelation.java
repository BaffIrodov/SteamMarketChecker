package net.smc.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ActualCurrencyRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private Double convertedPrice;
    private Double currencyPrice;
    private Double relation;
    private boolean archive;

    public ActualCurrencyRelation(Double convertedPrice, Double currencyPrice, Double relation) {
         this.convertedPrice = convertedPrice;
         this.currencyPrice = currencyPrice;
         this.relation = relation;
    }
}
