package com.cineverse.cineverse_backend.domain.content.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentReviewUpdateRequestDTO {

    private String reviewTitle;

    private String reviewText;

    private boolean spoiler;

    private Integer score;
}
