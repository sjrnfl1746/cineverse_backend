package com.cineverse.cineverse_backend.domain.event.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {

    private Long eventId;

    private String title;

    private String description;

    private LocalDate startAt;

    private LocalDate endAt;

    private String posterUrl;
}
