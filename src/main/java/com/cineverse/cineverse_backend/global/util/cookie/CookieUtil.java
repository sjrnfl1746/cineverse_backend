package com.cineverse.cineverse_backend.global.util.cookie;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final CookieProperties cookieProperties;

    // refreshToken 쿠키 생성
    public ResponseCookie createRefreshTokenCookie(String refreshToken, boolean rememberMe) {

        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true) // 프론트에서 JS로 읽는거 방지
                .secure(cookieProperties.isSecure()) // 로컬 개발 - 배포 시 true로 변경
                .path("/") // 사이트 전체에서 쿠키 사용
                .sameSite(cookieProperties.getSameSite());

        // rememberMe 조건 확인
        if (rememberMe) {
            builder.maxAge(Duration.ofDays(cookieProperties.getRefreshTokenExpDays()));
        }

        return builder.build();
    }

    // refreshToken 쿠키 삭제
    public ResponseCookie deleteRefreshTokenCookie() {

        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path("/")
                .sameSite(cookieProperties.getSameSite())
                .maxAge(0)
                .build();
    }
}
