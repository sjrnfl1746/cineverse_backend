package com.cineverse.cineverse_backend.domain.content.entity;

import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "content_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentReviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    private String reviewTitle;

    @Column(columnDefinition = "TEXT")
    private String reviewText;

    @Builder.Default
    private boolean spoiler = false;

    @Builder.Default
    private boolean deleted = false;

    // 리뷰 삭제
    public void deleteReview() {
        this.deleted = true;
    }

    // 리뷰 수정
    public void updateReview(String reviewTitle, String reviewText, boolean spoiler) {
        this.reviewTitle = reviewTitle;
        this.reviewText = reviewText;
        this.spoiler = spoiler;
    }
}
