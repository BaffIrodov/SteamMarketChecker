package net.smc.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.smc.dto.ActualCurrencyRelationDto;
import net.smc.dto.LotDto;
import net.smc.dto.SteamItemDto;
import net.smc.entities.QActualCurrencyRelation;
import net.smc.entities.QSteamItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActualCurrencyRelationReader {
    private static final QActualCurrencyRelation qActualCurrencyRelation = QActualCurrencyRelation.actualCurrencyRelation;
    private final JPAQueryFactory queryFactory;

    public static QBean<ActualCurrencyRelationDto> getMappedSelectForActualCurrencyRelationDto() {
        return Projections.bean(
                ActualCurrencyRelationDto.class,
                qActualCurrencyRelation.id,
                qActualCurrencyRelation.convertedPrice,
                qActualCurrencyRelation.currencyPrice,
                qActualCurrencyRelation.relation,
                qActualCurrencyRelation.archive
        );
    }

    public ActualCurrencyRelationDto getActualCurrencyRelationDto() {
        return queryFactory.from(qActualCurrencyRelation)
                .select(getMappedSelectForActualCurrencyRelationDto())
                .where(qActualCurrencyRelation.archive.isFalse())
                .fetchFirst();
    }
}
