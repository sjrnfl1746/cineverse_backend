package com.cineverse.cineverse_backend.domain.event.repository;

import com.cineverse.cineverse_backend.domain.event.entity.EventAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventAnnouncementRepository extends JpaRepository<EventAnnouncement, Long> , EventAnnouncementRepositoryCustom{

    // eventId로 이벤트 결과 발표 조회
    Optional<EventAnnouncement> findByEvent_EventIdAndDeletedFalse(Long eventId);
}
