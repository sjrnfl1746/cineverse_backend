package com.cineverse.cineverse_backend.global.security.jwt;

import com.cineverse.cineverse_backend.global.security.user.CustomUserDetails;
import com.cineverse.cineverse_backend.global.security.user.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    // permitAll 경로인 것들
    private static final String[] PERMIT_ALL_LIST = {
            "/api/auth"
    };

    // permitAll 경로인지 확인
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();

        return Arrays.stream(PERMIT_ALL_LIST).anyMatch(path::startsWith);
    }

    // request header 에서 JWT 토큰 추출
    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        // bearerToken이 존재하고 문자열이 "Bearer "로 시작하는지 확인
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후만 반환
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request header 에서 JWT 토큰 추출
        String accessToken = resolveToken(request);

        // accessToken 유효성 검사
        if (accessToken != null) {

            // 토큰 유효성 검증
            if (jwtProvider.validateToken(accessToken)) { // 토큰이 유효한 경우, 토큰에서 Authentication 객체를 가져와서 SecurityContext에 저장

                // 토큰에 있는 정보 가져오기
                Long userId = jwtProvider.getUserId(accessToken);
                CustomUserDetails userDetails = customUserDetailsService.loadUserByUserId(userId);

                // 현재 실행중인 스레드에 인증 정보를 저장
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else { // 토큰이 유효하지 않은 경우

                // 인증 정보 부족
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
