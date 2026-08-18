package com.cineverse.cineverse_backend.domain.event.repository;

import com.cineverse.cineverse_backend.domain.event.entity.Event;
import com.cineverse.cineverse_backend.domain.event.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

    // 삭제되지 않은 전체 이벤트 개수
    Long countByDeletedFalse();

    // 진행중인 이벤트 개수 - deleted = false
    Long countByEventStatusAndDeletedFalse(EventStatus eventStatus);

    Optional<Event> findByEventIdAndDeletedFalse(Long eventId);

    // 이벤트 시작 상태 변경 - READY - PUBLISHED
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Event e SET e.eventStatus = :publishedStatus
                 WHERE e.eventStatus = :readyStatus AND e.startAt IS NOT NULL
                   AND e.startAt <= :today AND e.deleted = false
    """)
    int publishedEvents(@Param("today") LocalDate today, @Param("readyStatus") EventStatus readyStatus, @Param("publishedStatus") EventStatus publishedStatus);

    // 이벤트 종료 상태 변경 - PUBLISHED - RESULT_PENDING
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Event e SET e.eventStatus = :resultPendingStatus
                 WHERE e.eventStatus = :publishedStatus AND e.endAt IS NOT NULL
                   AND e.endAt <= :today AND e.deleted = false
    """)
    int resultPendingEvents(@Param("today") LocalDate today, @Param("publishedStatus") EventStatus publishedStatus, @Param("resultPendingStatus") EventStatus resultPendingStatus);
}
