package com.cineverse.cineverse_backend.global.security.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtProperties { // JWT 설정값들

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration-minutes}")
    private Long accessTokenExpMinutes;

    @Value("${jwt.refresh-token-expiration-days}")
    private Long refreshTokenExpDays;
}
