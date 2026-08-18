package com.cineverse.cineverse_backend.domain.content.entity;

import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "content_rating",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_rating_user_content",
                        columnNames = {"user_id", "content_id"})
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentRating extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentRatingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_review_id")
    private ContentReview contentReview;

    private Integer score;

    // 별점 수정
    public void updateRating(Integer score) {
        this.score = score;
    }
}
