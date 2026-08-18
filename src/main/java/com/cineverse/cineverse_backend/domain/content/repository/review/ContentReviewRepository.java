package com.cineverse.cineverse_backend.domain.content.repository.review;

import com.cineverse.cineverse_backend.domain.content.entity.ContentReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentReviewRepository extends JpaRepository<ContentReview, Long>, ContentReviewRepositoryCustom {

    // userId로 작성한 리뷰 개수 조회
    Long countByUser_UserId(Long userId);
}
