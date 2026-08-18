package com.cineverse.cineverse_backend.domain.event.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventAnnounceRequestDTO {

    private String title;

    private String description;
}
