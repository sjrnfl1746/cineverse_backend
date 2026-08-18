package com.cineverse.cineverse_backend.domain.event.repository;

import com.cineverse.cineverse_backend.domain.event.dto.response.EventAnnounceWinnerResponseDTO;
import com.cineverse.cineverse_backend.domain.event.entity.QEvent;
import com.cineverse.cineverse_backend.domain.event.entity.QEventAnnouncement;
import com.querydsl.core.types.Projections;
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
public class EventAnnouncementRepositoryCustomImpl implements EventAnnouncementRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QEvent event = QEvent.event;
    private final QEventAnnouncement eventAnnouncement = QEventAnnouncement.eventAnnouncement;

    @Transactional
    @Override
    public Page<EventAnnounceWinnerResponseDTO> findAllEventAnnouncementList(Pageable pageable) {

        List<EventAnnounceWinnerResponseDTO> eventAnnounceList = queryFactory
                .select(Projections.fields(
                        EventAnnounceWinnerResponseDTO.class,
                        eventAnnouncement.eventAnnouncementId,
                        event.eventId,
                        event.title.as("eventTitle"),
                        eventAnnouncement.title,
                        eventAnnouncement.createdAt
                ))
                .from(eventAnnouncement)
                .leftJoin(event)
                .on(
                        eventAnnouncement.event.eventId.eq(event.eventId)
                )
                .where(
                        eventAnnouncement.deleted.eq(false)
                )
                .orderBy(eventAnnouncement.createdAt.desc(), eventAnnouncement.eventAnnouncementId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(eventAnnouncement.count())
                .from(eventAnnouncement)
                .where(
                        eventAnnouncement.deleted.eq(false)
                )
                .fetchOne();

        return new PageImpl<>(eventAnnounceList, pageable, Optional.ofNullable(total).orElse(0L));
    }
}
