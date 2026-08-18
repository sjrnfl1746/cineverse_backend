package com.cineverse.cineverse_backend.domain.content.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentReviewRequestDTO {

    private Long contentId; // 콘텐츠 Id

    private Integer score; // 별점

    private String reviewTitle; // 한줄평

    private String reviewText; // 리뷰

    private boolean spoiler; // 스포일러 여부
}
