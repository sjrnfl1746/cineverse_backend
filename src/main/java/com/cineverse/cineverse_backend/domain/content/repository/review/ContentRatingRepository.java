package com.cineverse.cineverse_backend.domain.content.repository.review;

import com.cineverse.cineverse_backend.domain.content.entity.ContentRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRatingRepository extends JpaRepository<ContentRating, Long> {

    // contentReviewId로 별점 조회
    ContentRating findByContentReview_ContentReviewId(Long contentReviewId);
}
