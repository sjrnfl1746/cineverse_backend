package com.cineverse.cineverse_backend.domain.content.service.review;

import com.cineverse.cineverse_backend.domain.content.dto.request.ContentReviewRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.ContentReviewUpdateRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.SearchContentReviewDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewListResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ContentReviewService {

    // 리뷰 등록
    void addContentReview(Long userId, ContentReviewRequestDTO contentReviewRequestDTO);

    // 리뷰 단건 조회
    ContentReviewResponseDTO getContentReviewByIdAndUserId(Long userId, Long contentReviewId);

    // 리뷰 목록 조회
    Slice<ContentReviewListResponseDTO> getContentReviewList(SearchContentReviewDTO searchContentReviewDTO, Pageable pageable);

    // 리뷰 삭제
    void removeContentReview(Long userId, Long contentReviewId);

    // 리뷰 수정
    void updateContentReview(Long userId, Long contentReviewId, ContentReviewUpdateRequestDTO contentReviewUpdateRequestDTO);

    Page<ContentReviewListResponseDTO> getUserContentReviewList(Long userId, Pageable pageable);
}
