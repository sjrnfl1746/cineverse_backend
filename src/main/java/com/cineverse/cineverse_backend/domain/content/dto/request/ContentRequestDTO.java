package com.cineverse.cineverse_backend.domain.content.dto.request;

import com.cineverse.cineverse_backend.domain.content.enums.AgeRating;
import com.cineverse.cineverse_backend.domain.content.enums.ContentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class ContentRequestDTO {

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

    // 장르 id들
    List<Long> genreIds;
}
