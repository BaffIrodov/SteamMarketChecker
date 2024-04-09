package net.smc.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.smc.dto.ActiveNameDto;
import net.smc.entities.QActiveName;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActiveNameReader {
    private static final QActiveName qActiveName = QActiveName.activeName;
    private final JPAQueryFactory queryFactory;


    public static QBean<ActiveNameDto> getMappedSelectForActiveNameDto() {
        return Projections.bean(
                ActiveNameDto.class,
                qActiveName.id,
                qActiveName.itemName,
                qActiveName.parseItemCount,
                qActiveName.parsePeriod,
                qActiveName.lastParseDate,
                qActiveName.archive,
                qActiveName.forceUpdate
        );
    }

    public List<ActiveNameDto> getAllActiveNames(boolean showArchive) {
        return queryFactory.from(qActiveName)
                .select(getMappedSelectForActiveNameDto())
                .where(qActiveName.archive.eq(showArchive))
                .fetch();
    }

    public List<ActiveNameDto> getActiveNameByIds(List<Long> ids) {
        return queryFactory.from(qActiveName)
                .select(getMappedSelectForActiveNameDto())
                .where(qActiveName.id.in(ids))
                .fetch();
    }
}
