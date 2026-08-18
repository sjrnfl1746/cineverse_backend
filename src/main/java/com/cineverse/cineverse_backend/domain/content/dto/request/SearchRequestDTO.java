package com.cineverse.cineverse_backend.domain.content.dto.request;

import com.cineverse.cineverse_backend.domain.content.enums.ContentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDTO {

    private String keyword;
    private ContentStatus contentStatus;
}
