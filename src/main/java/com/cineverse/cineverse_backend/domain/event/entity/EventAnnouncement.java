package com.cineverse.cineverse_backend.domain.event.entity;

import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@Entity
@Table(name = "event_announcement")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EventAnnouncement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventAnnouncementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private boolean deleted = false;

    // 이벤트 결과 발표 수정
    public void updateEventAnnouncement(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // 이벤트 결과 발표 삭제
    public void deleteEventAnnouncement() {
        this.deleted = true;
    }
}
