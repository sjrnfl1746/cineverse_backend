package com.cineverse.cineverse_backend.domain.event.repository;

import com.cineverse.cineverse_backend.domain.event.dto.request.SearchEventRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventListResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventOneResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventResponseDTO;
import com.cineverse.cineverse_backend.domain.event.entity.QEvent;
import com.cineverse.cineverse_backend.domain.event.enums.EventStatus;
import com.cineverse.cineverse_backend.domain.image.entity.QImage;
import com.cineverse.cineverse_backend.domain.image.enums.ImageTargetType;
import com.cineverse.cineverse_backend.domain.image.enums.ImageType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QEvent event = QEvent.event;
    private final QImage image = QImage.image;

    @Override
    public List<EventResponseDTO> findAllEvents() {
        return queryFactory
                .select(Projections.fields(
                        EventResponseDTO.class,
                        event.eventId,
                        event.title,
                        event.description,
                        event.startAt,
                        event.endAt,
                        image.path.as("posterUrl")
                ))
                .from(event)
                .leftJoin(image)
                .on(
                        image.targetId.eq(event.eventId),
                        image.targetType.eq(ImageTargetType.EVENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )
                .where(
                        event.eventStatus.eq(EventStatus.PUBLISHED),
                        event.deleted.eq(false)
                )
                .orderBy(event.createdAt.desc(), event.eventId.desc())
                .limit(5)
                .fetch();
    }

    @Transactional
    @Override
    public Page<EventListResponseDTO> findByStatusAndKeyword(SearchEventRequestDTO searchEventRequestDTO, Pageable pageable) {

        List<EventListResponseDTO> eventList = queryFactory
                .select(Projections.fields(
                        EventListResponseDTO.class,
                        event.eventId,
                        event.title,
                        event.description,
                        event.startAt,
                        event.endAt,
                        event.eventStatus,
                        event.createdAt
                ))
                .from(event)
                .where(
                        keywordContains(searchEventRequestDTO.getKeyword()),
                        statusEq(searchEventRequestDTO.getEventStatus()),
                        event.deleted.eq(false)
                )
                .orderBy(event.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(event.count())
                .from(event)
                .where(
                        keywordContains(searchEventRequestDTO.getKeyword()),
                        statusEq(searchEventRequestDTO.getEventStatus()),
                        event.deleted.eq(false)
                )
                .fetchOne();

        return new PageImpl<>(eventList, pageable, Optional.ofNullable(total).orElse(0L));
    }

    @Override
    public EventOneResponseDTO findEventById(Long eventId) {
        return queryFactory
                .select(Projections.fields(
                        EventOneResponseDTO.class,
                        event.eventId,
                        event.title,
                        event.description,
                        event.startAt,
                        event.endAt,
                        event.eventStatus,
                        image.path.as("posterUrl"),
                        event.createdAt
                ))
                .from(event)
                .leftJoin(image)
                .on(
                        image.targetId.eq(event.eventId),
                        image.targetType.eq(ImageTargetType.EVENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )
                .where(
                        event.eventId.eq(eventId),
                        event.deleted.eq(false)
                )
                .fetchOne();
    }

    @Override
    public EventOneResponseDTO findPublishedEventById(Long eventId) {
        return queryFactory
                .select(Projections.fields(
                        EventOneResponseDTO.class,
                        event.eventId,
                        event.title,
                        event.description,
                        event.startAt,
                        event.endAt,
                        event.eventStatus,
                        image.path.as("posterUrl"),
                        event.createdAt
                ))
                .from(event)
                .leftJoin(image)
                .on(
                        image.targetId.eq(event.eventId),
                        image.targetType.eq(ImageTargetType.EVENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )
                .where(
                        event.eventId.eq(eventId),
                        event.eventStatus.eq(EventStatus.PUBLISHED),
                        event.deleted.eq(false)
                )
                .fetchOne();
    }

    private BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            return null;
        }

        String trimmedKeyword = keyword.trim();

        return event.title.containsIgnoreCase(trimmedKeyword);
    }

    private BooleanExpression statusEq(EventStatus status) {
        return status != null
                ? event.eventStatus.eq(status)
                : null;
    }
}
