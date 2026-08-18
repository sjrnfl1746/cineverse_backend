package com.cineverse.cineverse_backend.domain.content.entity;


import com.cineverse.cineverse_backend.domain.content.enums.AgeRating;
import com.cineverse.cineverse_backend.domain.content.enums.ContentStatus;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@Entity
@Table(name = "content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Content extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    private String title;

    private String ogTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate releaseAt;

    private LocalDate endAt;

    private int runningTime;

    private String productionCountry;

    @Enumerated(EnumType.STRING)
    private AgeRating ageRating;

    @Enumerated(EnumType.STRING)
    private ContentStatus contentStatus;

    private String trailerUrl;

    @Builder.Default
    private boolean deleted = false;

    @Builder.Default
    private Long viewCnt = 0L;

    // 콘텐츠 삭제
    public void delete() {
        this.deleted = true;
    }

    // 콘텐츠 복구
    public void restore() {
        this.deleted = false;
    }

    // 콘텐츠 수정
    public void update(String title, String ogTitle, String description, LocalDate releaseAt, LocalDate endAt,
                       int runningTime, String productionCountry, AgeRating ageRating, ContentStatus contentStatus, String trailerUrl) {
        this.title = title;
        this.ogTitle = ogTitle;
        this.description = description;
        this.releaseAt = releaseAt;
        this.endAt = endAt;
        this.runningTime = runningTime;
        this.productionCountry = productionCountry;
        this.ageRating = ageRating;
        this.contentStatus = contentStatus;
        this.trailerUrl = trailerUrl;
    }

    // 조회수 + 1
    public void updateViewCnt() {
        this.viewCnt = this.viewCnt + 1;
    }

}
