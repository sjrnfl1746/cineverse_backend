package com.cineverse.cineverse_backend.domain.content.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchContentReviewDTO {

    private String type;

    private String keyword;
}
