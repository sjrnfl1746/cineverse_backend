package com.cineverse.cineverse_backend.global.security.jwt;

import com.cineverse.cineverse_backend.global.exception.BusinessException;
import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    // refreshToken 저장
    public void saveRefreshToken(String refreshToken, Long userId, boolean rememberMe) {

        // 만료 시간
        Duration ttl = Duration.ofDays(jwtProperties.getRefreshTokenExpDays());

        // refreshToken 정보
        RefreshTokenInfo info = RefreshTokenInfo.builder()
                .userId(userId)
                .rememberMe(rememberMe)
                .build();

        // redis에 refreshToken 저장
        redisTemplate.opsForValue().set(getKey(refreshToken), info, ttl);
    }

    // refreshToken 삭제
    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete(getKey(refreshToken));
    }

    // refreshToken에 담긴 정보 반환
    public RefreshTokenInfo getRefreshTokenInfo(String refreshToken) {

        Object value = redisTemplate.opsForValue().get(getKey(refreshToken));

        if (value == null) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        RefreshTokenInfo info =
                objectMapper.convertValue(value, RefreshTokenInfo.class);

        if(info == null) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return info;
    }

    // 키 값 가져오기
    private String getKey(String refreshToken) {
        return "RT:" + refreshToken;
    }
}
