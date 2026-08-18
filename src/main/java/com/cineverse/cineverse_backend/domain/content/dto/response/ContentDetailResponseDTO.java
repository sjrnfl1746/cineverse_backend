package com.cineverse.cineverse_backend.domain.content.dto.response;

import com.cineverse.cineverse_backend.domain.content.enums.AgeRating;
import com.cineverse.cineverse_backend.domain.content.enums.ContentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentDetailResponseDTO { // 콘텐츠 단건 조회

    private Long contentId;

    private String title;

    private String ogTitle;

    private String description;

    private LocalDate releaseAt;

    private LocalDate endAt;

    private int runningTime;

    private String productionCountry;

    private AgeRating ageRating;

    private ContentStatus contentStatus;

    private String trailerUrl;

    // 장르
    private List<GenreResponseDTO> genres;

    // 포스터 url
    private String posterUrl;

    // 영상 url
    private String videoUrl;

    // 찜목록 여부
    private boolean wishlisted;
}
