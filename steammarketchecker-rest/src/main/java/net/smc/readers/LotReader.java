package net.smc.readers;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.QList;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.smc.dto.LotDto;
import net.smc.dto.SteamItemDto;
import net.smc.entities.QLot;
import net.smc.entities.QSteamItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LotReader {
    private static final QLot qLot = QLot.lot;
    private static final QSteamItem qSteamItem = QSteamItem.steamItem;
    private final JPAQueryFactory queryFactory;

    public static QBean<LotDto> getMappedSelectForLotDto() {
        return Projections.bean(
                LotDto.class,
                qLot.id,
                qLot.listingId,
                qLot.assetId,
                qLot.assetId,
                Projections.bean(SteamItemDto.class,
                        qSteamItem.id,
                        qSteamItem.name,
                        qSteamItem.minPrice,
                        qSteamItem.parseQueueId,
                        qSteamItem.parseDate,
                        qSteamItem.forceUpdate,
                        qSteamItem.steamItemType).as("skin"),
                qLot.completeness,
                qLot.profitability,
                qLot.actual,
                qLot.profit,
                qLot.convertedPrice,
                qLot.convertedFee,
                qLot.realPrice,
                qLot.priceCalculatingDate,
                qLot.parseDate,
                qLot.stickersAsString,
                qLot.positionInListing
        );
    }

    public List<LotDto> getAllLots(Boolean onlyActual, Boolean onlyCompleteness, Boolean onlyProfitability) {
        return queryFactory.from(qLot)
                .select(getMappedSelectForLotDto())
                .where(onlyActual ? qLot.actual.isTrue() : null)
                .where(onlyCompleteness ? qLot.completeness.isTrue() : null)
                .where(onlyProfitability ? qLot.profitability.isTrue() : null)
                .orderBy(qLot.profit.desc().nullsLast())
                .fetch();
    }

    public List<LotDto> getLotDtoByIds(List<Long> ids) {
        return queryFactory.from(qLot)
                .select(getMappedSelectForLotDto())
                .where(qLot.id.in(ids))
                .fetch();
    }
}
