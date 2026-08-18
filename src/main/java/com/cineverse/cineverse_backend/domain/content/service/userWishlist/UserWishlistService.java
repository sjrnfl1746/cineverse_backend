package com.cineverse.cineverse_backend.domain.content.service.userWishlist;

import com.cineverse.cineverse_backend.domain.user.dto.response.UserWishlistResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserWishlistService {

    // 찜목록 등록
    void addWishlist(Long userId, Long contentId);

    // 찜목록 제거
    void removeWishlist(Long userId, Long contentId);

    // 사용자 찜목록 조회
    Page<UserWishlistResponseDTO> getAllUserWishlists(Long userId, Pageable pageable);
}
