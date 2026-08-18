package com.cineverse.cineverse_backend.domain.content.repository.userWishlist;

import com.cineverse.cineverse_backend.domain.user.dto.response.UserWishlistResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserWishlistRepositoryCustom {

    // 사용자 찜목록 리스트 조회
    Page<UserWishlistResponseDTO> findAllUserWishlist(Long userId, Pageable pageable);
}
