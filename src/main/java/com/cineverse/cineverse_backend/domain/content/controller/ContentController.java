package com.cineverse.cineverse_backend.domain.content.controller;

import com.cineverse.cineverse_backend.domain.content.dto.request.SearchRandomContentRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentDetailResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentSliceListResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentTop5ResponseDTO;
import com.cineverse.cineverse_backend.domain.content.service.content.ContentService;
import com.cineverse.cineverse_backend.domain.content.service.userWishlist.UserWishlistService;
import com.cineverse.cineverse_backend.global.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
@Tag(name = "Content", description = "콘텐츠 관련 API")
public class ContentController {

    private final ContentService contentService;
    private final UserWishlistService userWishlistService;

    @Operation(summary = "조회수 TOP 6 콘텐츠 조회", description = "조회수 높은 영화 6개 조회")
    @GetMapping("/top6")
    public ResponseEntity<List<ContentTop5ResponseDTO>> getTop6Content() {
        return ResponseEntity.ok(contentService.getTop6Content());
    }

    @Operation(summary = "랜덤 20개 콘텐츠 조회", description = "랜덤한 20개의 콘텐츠를 조회")
    @GetMapping("/random")
    public ResponseEntity<Slice<ContentSliceListResponseDTO>> getRandomContent(
            SearchRandomContentRequestDTO searchRandomContentRequestDTO,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(contentService.getRandomContentSlice(searchRandomContentRequestDTO, pageable));
    }

    @Operation(summary = "콘텐츠 단건 조회", description = "contentId로 콘텐츠 단건 조회")
    @GetMapping("/{contentId}")
    public ResponseEntity<ContentDetailResponseDTO> getContentById(@PathVariable Long contentId,
                                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = (userDetails != null) ? userDetails.getUserId() : null;

        return ResponseEntity.ok(contentService.getPublishedContentById(userId, contentId));
    }

    @Operation(summary = "찜목록 저장", description = "해당 콘텐츠를 찜목록에 저장")
    @PostMapping("/{contentId}/wishlist")
    public ResponseEntity<Void> addWishlist(@PathVariable Long contentId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        userWishlistService.addWishlist(userDetails.getUserId(), contentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "찜목록 삭제", description = "해당 콘텐츠를 찜목록에서 제거")
    @DeleteMapping("/{contentId}/wishlist")
    public ResponseEntity<Void> removeWishlist(@PathVariable Long contentId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        userWishlistService.removeWishlist(userDetails.getUserId(), contentId);
        return ResponseEntity.noContent().build();
    }
}
