package net.smc.readers;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.smc.dto.ParseQueueDto;
import net.smc.entities.ParseQueue;
import net.smc.entities.QParseQueue;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ParseQueueReader {
    private static final QParseQueue qParseQueue = QParseQueue.parseQueue;
    private final JPAQueryFactory queryFactory;

    public static QBean<ParseQueueDto> getMappedSelectForParseQueueDto() {
        return Projections.bean(
                ParseQueueDto.class,
                qParseQueue.id,
                qParseQueue.importance,
                qParseQueue.parseType,
                qParseQueue.parseTarget,
                qParseQueue.parseUrl,
                qParseQueue.archive
        );
    }

    public List<ParseQueueDto> getAllParseQueues(Boolean archive) {
        return queryFactory.from(qParseQueue)
                .select(getMappedSelectForParseQueueDto())
                .where(archive ? qParseQueue.archive.isTrue() : qParseQueue.archive.isFalse())
                .fetch();
    }

    public List<ParseQueueDto> getParseQueueDtoByIds(List<Long> ids) {
        return queryFactory.from(qParseQueue)
                .select(getMappedSelectForParseQueueDto())
                .where(qParseQueue.id.in(ids))
                .fetch();
    }

    public Map<String, ParseQueueDto> getMapQueueByTarget(List<String> parseTargets, boolean archive) {
        return queryFactory.from(qParseQueue)
                .where(qParseQueue.archive.eq(archive).and(qParseQueue.parseTarget.in(parseTargets)))
                .select(getMappedSelectForParseQueueDto())
                .fetch().stream().collect(Collectors.toMap(ParseQueueDto::getParseTarget, e -> e));
    }
}
