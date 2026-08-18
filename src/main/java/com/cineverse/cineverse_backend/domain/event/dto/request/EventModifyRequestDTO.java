package com.cineverse.cineverse_backend.domain.event.dto.request;

import com.cineverse.cineverse_backend.domain.event.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventModifyRequestDTO {

    private String title;

    private String description;

    private LocalDate startAt;

    private LocalDate endAt;

    private EventStatus eventStatus;
}
