package net.smc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.smc.entities.ActualCurrencyRelation;

@Data
@NoArgsConstructor
public class ActualCurrencyRelationDto {
    private Long id;
    private Double convertedPrice;
    private Double currencyPrice;
    private Double relation;
    private boolean archive;

    public ActualCurrencyRelationDto(ActualCurrencyRelation actualCurrencyRelation) {
        this.id = actualCurrencyRelation.getId();
        this.convertedPrice = actualCurrencyRelation.getConvertedPrice();
        this.currencyPrice = actualCurrencyRelation.getCurrencyPrice();
        this.relation = actualCurrencyRelation.getRelation();
        this.archive = actualCurrencyRelation.isArchive();
    }
}
