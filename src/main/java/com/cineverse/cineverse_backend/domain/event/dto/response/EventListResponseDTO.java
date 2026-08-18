package com.cineverse.cineverse_backend.domain.event.dto.response;

import com.cineverse.cineverse_backend.domain.event.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventListResponseDTO {

    private Long eventId;

    private String title;

    private String description;

    private LocalDate startAt;

    private LocalDate endAt;

    private EventStatus eventStatus;

    private LocalDateTime createdAt;
}
