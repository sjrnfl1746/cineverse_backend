package com.cineverse.cineverse_backend.domain.content.service.userWishlist;

import com.cineverse.cineverse_backend.domain.content.entity.Content;
import com.cineverse.cineverse_backend.domain.content.repository.content.ContentRepository;
import com.cineverse.cineverse_backend.domain.content.repository.userWishlist.UserWishlistRepository;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserWishlistResponseDTO;
import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.domain.user.entity.UserWishlist;
import com.cineverse.cineverse_backend.domain.user.enums.UserStatus;
import com.cineverse.cineverse_backend.domain.user.repository.UserRepository;
import com.cineverse.cineverse_backend.global.exception.BusinessException;
import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWishlistServiceImpl implements UserWishlistService {

    private final UserWishlistRepository userWishlistRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    @Transactional
    @Override
    public void addWishlist(Long userId, Long contentId) {

        // 존재여부 확인 -> 존재 시 리턴
        boolean exists = userWishlistRepository.existsByUser_UserIdAndContent_ContentId(userId, contentId);
        if (exists) {
            return;
        }

        // 사용자 존재 여부 확인
        User user = userRepository.findByUserIdAndStatus(userId, UserStatus.ACTIVE).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 콘텐츠 존재 여부 확인
        Content content = contentRepository.findById(contentId).orElseThrow(
                () -> new RuntimeException("콘텐츠가 존재하지 않습니다."));

        UserWishlist userWishlist = UserWishlist.builder()
                .user(user)
                .content(content)
                .build();
        userWishlistRepository.save(userWishlist);
    }

    @Transactional
    @Override
    public void removeWishlist(Long userId, Long contentId) {

        // 존재여부 확인 -> 존재 시 리턴
        boolean exists = userWishlistRepository.existsByUser_UserIdAndContent_ContentId(userId, contentId);
        if (!exists) {
            return;
        }
        userWishlistRepository.deleteByUser_UserIdAndContent_ContentId(userId, contentId);
    }

    @Override
    public Page<UserWishlistResponseDTO> getAllUserWishlists(Long userId, Pageable pageable) {
        return userWishlistRepository.findAllUserWishlist(userId, pageable);
    }
}
