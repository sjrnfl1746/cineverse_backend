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
public class EventAnnouncementResponseDTO {

    private Long eventAnnouncementId;

    private String title;

    private String description;

    private LocalDateTime createdAt;
}
