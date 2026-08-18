package com.cineverse.cineverse_backend.domain.content.repository.review;

import com.cineverse.cineverse_backend.domain.content.dto.request.SearchContentReviewDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewListResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ContentReviewRepositoryCustom {

    ContentReviewResponseDTO findContentReviewByContentReviewId(Long userId, Long contentReviewId);

    // 리뷰 10개 조회 - 무한 스크롤
    Slice<ContentReviewListResponseDTO> findContentReviews(SearchContentReviewDTO searchContentReviewDTO, Pageable pageable);

    // 사용자 작성 리뷰 3개 조회
    List<ContentReviewListResponseDTO> findContentReviewByUserId(Long userId);

    // 사용자 작성 리뷰 조회 - 페이징
    Page<ContentReviewListResponseDTO> findUserContentReviews(Long userId, Pageable pageable);
}
