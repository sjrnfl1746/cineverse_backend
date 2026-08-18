package com.cineverse.cineverse_backend.domain.user.controller;

import com.cineverse.cineverse_backend.domain.content.service.userWishlist.UserWishlistService;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserWishlistResponseDTO;
import com.cineverse.cineverse_backend.global.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
@Tag(name = "User Wishlist", description = "사용자 찜 관련 API")
public class UserWishlistController {

    private final UserWishlistService userWishlistService;

    @Operation(summary = "내 찜목록 조회", description = "내 찜목록 조회")
    @GetMapping
    public ResponseEntity<Page<UserWishlistResponseDTO>> getAllUserWishlists(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                             @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(userWishlistService.getAllUserWishlists(userDetails.getUserId(), pageable));
    }
}
