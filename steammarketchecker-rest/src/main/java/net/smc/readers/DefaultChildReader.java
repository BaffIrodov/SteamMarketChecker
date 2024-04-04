package net.smc.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.smc.dto.DefaultChildDto;
import net.smc.entities.QDefaultChild;
import net.smc.entities.QDefaultParent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DefaultChildReader {
    private static final QDefaultChild defaultChild = QDefaultChild.defaultChild;
    private static final QDefaultParent defaultParent = QDefaultParent.defaultParent;
    private final JPAQueryFactory queryFactory;


    public static QBean<DefaultChildDto> getMappedSelectForDefaultChildDto() {
        return Projections.bean(
                DefaultChildDto.class,
                defaultChild.id,
                defaultChild.defaultParentId,
                defaultChild.name,
                defaultChild.archive
        );
    }

    public List<DefaultChildDto> getAllDefaultChildren(boolean showArchive) {
        return queryFactory.from(defaultChild)
                .select(getMappedSelectForDefaultChildDto())
                .where(defaultChild.archive.eq(showArchive))
                .fetch();
    }

    public List<DefaultChildDto> getDefaultChildrenByParentId(Long defaultParentId) {
        return getDefaultChildrenByParentId(defaultParentId, false);
    }

    public List<DefaultChildDto> getDefaultChildrenByParentId(Long defaultParentId, Boolean showArchive) {
        return queryFactory.from(defaultChild)
                .leftJoin(defaultParent).on(defaultParent.id.eq(defaultChild.defaultParentId))
                .select(getMappedSelectForDefaultChildDto())
                .where(defaultParent.id.eq(defaultParentId)
                        .and(defaultChild.archive.eq(showArchive)))
                .fetch();
    }
}
