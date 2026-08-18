package com.cineverse.cineverse_backend.domain.content.repository.userWishlist;

import com.cineverse.cineverse_backend.domain.user.entity.UserWishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWishlistRepository extends JpaRepository<UserWishlist, Long>, UserWishlistRepositoryCustom {

    // 찜 여부 확인
    boolean existsByUser_UserIdAndContent_ContentId(Long userId, Long contentId);

    // 찜 제거
    void deleteByUser_UserIdAndContent_ContentId(Long userId, Long contentId);

    // userId로 찜한 개수 조회
    Long countByUser_UserId(Long userId);
}
