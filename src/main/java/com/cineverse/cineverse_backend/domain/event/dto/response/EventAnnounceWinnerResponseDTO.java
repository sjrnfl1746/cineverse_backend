package com.cineverse.cineverse_backend.domain.event.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventAnnounceWinnerResponseDTO {

    private Long eventAnnouncementId;

    private Long eventId;

    private String eventTitle;

    private String title;

    private LocalDateTime createdAt;
}
