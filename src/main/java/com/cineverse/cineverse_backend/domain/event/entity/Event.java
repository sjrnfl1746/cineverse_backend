package com.cineverse.cineverse_backend.domain.event.entity;

import com.cineverse.cineverse_backend.domain.event.enums.EventStatus;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@Entity
@Table(name = "event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate startAt;

    private LocalDate endAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EventStatus eventStatus = EventStatus.READY;

    @Builder.Default
    private boolean deleted = false;

    // 이벤트 수정
    public void updateEvent(String title, String description, LocalDate startAt, LocalDate endAt, EventStatus eventStatus) {
        this.title = title;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.eventStatus = eventStatus;
    }

    // 이벤트 삭제
    public void deleteEvent() {
        this.deleted = true;
    }

    // 이벤트 상태 변경
    public void modifyEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }
}
