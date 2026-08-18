package com.cineverse.cineverse_backend.domain.event.dto.request;

import com.cineverse.cineverse_backend.domain.event.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchEventRequestDTO {

    private EventStatus eventStatus;

    private String keyword;
}
