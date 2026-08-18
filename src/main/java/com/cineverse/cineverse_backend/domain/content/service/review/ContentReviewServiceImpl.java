package com.cineverse.cineverse_backend.domain.content.service.review;

import com.cineverse.cineverse_backend.domain.content.dto.request.ContentReviewRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.ContentReviewUpdateRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.SearchContentReviewDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewListResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewResponseDTO;
import com.cineverse.cineverse_backend.domain.content.entity.Content;
import com.cineverse.cineverse_backend.domain.content.entity.ContentRating;
import com.cineverse.cineverse_backend.domain.content.entity.ContentReview;
import com.cineverse.cineverse_backend.domain.content.repository.content.ContentRepository;
import com.cineverse.cineverse_backend.domain.content.repository.review.ContentRatingRepository;
import com.cineverse.cineverse_backend.domain.content.repository.review.ContentReviewRepository;
import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.domain.user.enums.UserRole;
import com.cineverse.cineverse_backend.domain.user.repository.UserRepository;
import com.cineverse.cineverse_backend.global.exception.BusinessException;
import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentReviewServiceImpl implements ContentReviewService {

    private final ContentReviewRepository contentReviewRepository;
    private final ContentRatingRepository contentRatingRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    @Transactional
    @Override
    public void addContentReview(Long userId, ContentReviewRequestDTO contentReviewRequestDTO) {

        // 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 콘텐츠 조회
        Content content = contentRepository.findById(contentReviewRequestDTO.getContentId()).orElseThrow(
                () -> new RuntimeException("콘텐츠가 존재하지 않습니다."));

        // 리뷰 등록
        ContentReview contentReview = ContentReview.builder()
                .user(user)
                .content(content)
                .reviewTitle(contentReviewRequestDTO.getReviewTitle())
                .reviewText(contentReviewRequestDTO.getReviewText())
                .spoiler(contentReviewRequestDTO.isSpoiler())
                .build();
        contentReviewRepository.save(contentReview);

        // 별점 등록
        ContentRating contentRating = ContentRating.builder()
                .user(user)
                .content(content)
                .contentReview(contentReview)
                .score(contentReviewRequestDTO.getScore())
                .build();
        contentRatingRepository.save(contentRating);
    }

    @Override
    public ContentReviewResponseDTO getContentReviewByIdAndUserId(Long userId, Long contentReviewId) {
        return contentReviewRepository.findContentReviewByContentReviewId(userId, contentReviewId);
    }

    @Override
    public Slice<ContentReviewListResponseDTO> getContentReviewList(SearchContentReviewDTO searchContentReviewDTO, Pageable pageable) {
        return contentReviewRepository.findContentReviews(searchContentReviewDTO, pageable);
    }

    @Transactional
    @Override
    public void removeContentReview(Long userId, Long contentReviewId) {

        // 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 리뷰 조회
        ContentReview contentReview = contentReviewRepository.findById(contentReviewId).orElseThrow(
                () -> new RuntimeException("리뷰가 존재하지 않습니다."));

        if (user.getRole().equals(UserRole.ROLE_USER) && !userId.equals(contentReview.getUser().getUserId())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // 콘텐츠 소프트 삭제
        contentReview.deleteReview();
    }

    @Transactional
    @Override
    public void updateContentReview(Long userId, Long contentReviewId, ContentReviewUpdateRequestDTO contentReviewUpdateRequestDTO) {

        // 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 리뷰 조회
        ContentReview contentReview = contentReviewRepository.findById(contentReviewId).orElseThrow(
                () -> new RuntimeException("리뷰가 존재하지 않습니다."));

        if (user.getRole().equals(UserRole.ROLE_USER) && !userId.equals(contentReview.getUser().getUserId())) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        // 별점 조회
        ContentRating contentRating = contentRatingRepository.findByContentReview_ContentReviewId(contentReviewId);

        // 리뷰 / 별점 수정
        contentReview.updateReview(contentReviewUpdateRequestDTO.getReviewTitle(),
                contentReviewUpdateRequestDTO.getReviewText(), contentReviewUpdateRequestDTO.isSpoiler());

        contentRating.updateRating(contentReviewUpdateRequestDTO.getScore());
    }

    @Override
    public Page<ContentReviewListResponseDTO> getUserContentReviewList(Long userId, Pageable pageable) {
        return contentReviewRepository.findUserContentReviews(userId, pageable);
    }
}
