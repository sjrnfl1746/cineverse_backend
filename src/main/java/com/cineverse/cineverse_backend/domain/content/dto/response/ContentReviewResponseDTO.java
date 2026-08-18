package com.cineverse.cineverse_backend.domain.content.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentReviewResponseDTO {

    private Long contentReviewId;

    private Long contentId;

    private String contentTitle;

    private LocalDate releaseAt;

    private String reviewTitle;

    private String reviewText;

    private Integer score;

    private String nickname;

    private String email;

    private String posterUrl;

    private LocalDateTime createdAt; // 리뷰 작성일

    private boolean writer; // 작성자 여부

    private boolean spoiler; // 스포일러 여부
}
