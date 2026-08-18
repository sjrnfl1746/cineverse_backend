package com.cineverse.cineverse_backend.global.security.user;

import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.domain.user.enums.UserStatus;
import com.cineverse.cineverse_backend.domain.user.repository.UserRepository;
import com.cineverse.cineverse_backend.global.exception.BusinessException;
import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 로그인 시 사용 - email로 조회
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new CustomUserDetails(user);
    }

    // JWT 인증 필터 시 사용 - userId로 조회
    public CustomUserDetails loadUserByUserId(Long userId) {

        User user = userRepository.findByUserIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new CustomUserDetails(user);
    }
}
