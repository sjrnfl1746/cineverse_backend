package com.cineverse.cineverse_backend.domain.content.controller;

import com.cineverse.cineverse_backend.domain.content.dto.request.ContentReviewRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.ContentReviewUpdateRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.SearchContentReviewDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewListResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewResponseDTO;
import com.cineverse.cineverse_backend.domain.content.service.review.ContentReviewService;
import com.cineverse.cineverse_backend.global.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
@Tag(name = "Content Review", description = "콘텐츠 리뷰 관련 API")
public class ContentReviewController {

    private final ContentReviewService contentReviewService;

    @Operation(summary = "콘텐츠 리뷰 등록", description = "콘텐츠 리뷰를 작성하는 메서드")
    @PostMapping
    public ResponseEntity<Void> addContentReview(@RequestBody ContentReviewRequestDTO contentReviewRequestDTO,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        contentReviewService.addContentReview(userDetails.getUserId(), contentReviewRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "콘텐츠 리뷰 단건 조회", description = "contentReviewId로 리뷰 조회")
    @GetMapping("/{contentReviewId}")
    public ResponseEntity<ContentReviewResponseDTO> getReviewById(@PathVariable("contentReviewId") Long contentReviewId,
                                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = (userDetails != null) ? userDetails.getUserId() : null;
        return ResponseEntity.ok(contentReviewService.getContentReviewByIdAndUserId(userId, contentReviewId));
    }

    @Operation(summary = "콘텐츠 리뷰 조회", description = "콘텐츠 리뷰 목록 조회")
    @GetMapping
    public ResponseEntity<Slice<ContentReviewListResponseDTO>> getReviewList(SearchContentReviewDTO searchContentReviewDTO, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(contentReviewService.getContentReviewList(searchContentReviewDTO, pageable));
    }

    @Operation(summary = "콘텐츠 리뷰 삭제", description = "콘텐츠 리뷰 삭제")
    @DeleteMapping("/{contentReviewId}")
    public ResponseEntity<Void> removeReview(@PathVariable Long contentReviewId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        contentReviewService.removeContentReview(userDetails.getUserId(), contentReviewId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "콘텐츠 리뷰 수정", description = "콘텐츠 리뷰 수정")
    @PutMapping("/{contentReviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable Long contentReviewId,
                                             @RequestBody ContentReviewUpdateRequestDTO contentReviewUpdateRequestDTO,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        contentReviewService.updateContentReview(userDetails.getUserId(), contentReviewId, contentReviewUpdateRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "사용자 콘텐츠 리뷰 조회", description = "사용자 콘텐츠 리뷰 조회")
    @GetMapping("/me")
    public ResponseEntity<Page<ContentReviewListResponseDTO>> getUserReviewList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                Pageable pageable) {
        return ResponseEntity.ok(contentReviewService.getUserContentReviewList(userDetails.getUserId(), pageable));
    }
}
