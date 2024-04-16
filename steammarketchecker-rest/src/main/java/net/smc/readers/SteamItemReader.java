package net.smc.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.smc.dto.SteamItemDto;
import net.smc.entities.QSteamItem;
import net.smc.entities.SteamItem;
import net.smc.enums.SteamItemType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SteamItemReader {
    private static final QSteamItem qSteamItem = QSteamItem.steamItem;
    private final JPAQueryFactory queryFactory;


    public static QBean<SteamItemDto> getMappedSelectForSteamItemDto() {
        return Projections.bean(
                SteamItemDto.class,
                qSteamItem.id,
                qSteamItem.name,
                qSteamItem.minPrice,
                qSteamItem.parseQueueId,
                qSteamItem.parseDate,
                qSteamItem.parsePeriod,
                qSteamItem.forceUpdate,
                qSteamItem.steamItemType
                );
    }

    public List<SteamItemDto> getAllSteamItems(SteamItemType steamItemType) {
        return queryFactory.from(qSteamItem)
                .select(getMappedSelectForSteamItemDto())
                .where(qSteamItem.steamItemType.eq(steamItemType))
                .orderBy(qSteamItem.minPrice.desc().nullsLast())
                .fetch();
    }

    public List<SteamItemDto> getItemDtoByIds(List<Long> ids) {
        return queryFactory.from(qSteamItem)
                .select(getMappedSelectForSteamItemDto())
                .where(qSteamItem.id.in(ids))
                .fetch();
    }

    public Map<String, Long> getAllSteamItemIdByName(List<String> nameList) {
        return queryFactory.from(qSteamItem)
                .select(qSteamItem)
                .where(qSteamItem.name.in(nameList))
                .fetch().stream()
                .collect(Collectors.toMap(SteamItem::getName, SteamItem::getId));
    }
}
