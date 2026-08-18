package com.cineverse.cineverse_backend.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final StringRedisTemplate redisTemplate;
    private SecretKey secretKey;

    // 애플리케이션 실행시 한번만 생성
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    // accessToken 생성
    public String generateAccessToken(Long userId, String email) {
        Date now = new Date();

        // 토큰 만료 시간
        Date exp = new Date(now.getTime() + Duration.ofMinutes(jwtProperties.getAccessTokenExpMinutes()).toMillis());

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(now)
                .expiration(exp)
                .signWith(secretKey)
                .compact();
    }

    // refreshToken 생성 - 아무 의미 없는 UUID 값으로 생성
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    // token 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("유효하지 않은 JWT 토큰", e);
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰", e);
        } catch (UnsupportedJwtException e) {
            log.warn("지원하지 않는 JWT 토큰", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 제공되지 않음", e);
        }

        return false;
    }

    // token 에서 userId 추출
    public Long getUserId(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    // JWT 토큰 복호화
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
