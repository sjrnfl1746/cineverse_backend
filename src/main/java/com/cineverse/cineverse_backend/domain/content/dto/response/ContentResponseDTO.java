package com.cineverse.cineverse_backend.domain.content.dto.response;

import com.cineverse.cineverse_backend.domain.content.enums.AgeRating;
import com.cineverse.cineverse_backend.domain.content.enums.ContentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponseDTO { // 콘텐츠 여러건 조회

    private Long contentId;

    private String title;

    private String ogTitle;

    private int runningTime;

    private String productionCountry;

    private AgeRating ageRating;

    private ContentStatus contentStatus;

    // 포스터 url
    private String path;
}
