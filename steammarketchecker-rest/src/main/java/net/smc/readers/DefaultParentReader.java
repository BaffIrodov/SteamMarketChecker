package net.smc.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.smc.dto.DefaultParentDto;
import net.smc.entities.QDefaultChild;
import net.smc.entities.QDefaultParent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DefaultParentReader {

    private static final QDefaultParent defaultParent = QDefaultParent.defaultParent;
    private static final QDefaultChild defaultChild = QDefaultChild.defaultChild;
    private final JPAQueryFactory queryFactory;


    public static QBean<DefaultParentDto> getMappedSelectForDefaultParentDto() {
        return Projections.bean(
                DefaultParentDto.class,
                defaultParent.id,
                defaultParent.name,
                defaultParent.archive
        );
    }

    public DefaultParentDto getDefaultParentById(Long defaultParentId) {
        return queryFactory.from(defaultParent)
                .select(getMappedSelectForDefaultParentDto())
                .where(defaultParent.id.eq(defaultParentId))
                .fetchFirst();
    }


    public DefaultParentDto getDefaultParentWithDefaultChildren(Long positionId) {
        return queryFactory.from(defaultParent)
                .leftJoin(defaultChild).on(defaultChild.defaultParentId.eq(defaultParent.id))
                .select(getMappedSelectForDefaultParentDto())
                .where(defaultParent.id.eq(positionId))
                .fetchOne();
    }

    public List<DefaultParentDto> getAllDefaultParents(boolean showArchive) {
        return queryFactory.from(defaultParent)
                .select(getMappedSelectForDefaultParentDto())
                .where(defaultParent.archive.eq(showArchive))
                .fetch();
    }
}
