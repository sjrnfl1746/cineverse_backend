package com.cineverse.cineverse_backend.domain.auth.controller;

import com.cineverse.cineverse_backend.domain.auth.dto.request.LoginRequestDTO;
import com.cineverse.cineverse_backend.domain.auth.dto.request.SignupRequestDTO;
import com.cineverse.cineverse_backend.domain.auth.dto.response.*;
import com.cineverse.cineverse_backend.domain.auth.service.AuthService;
import com.cineverse.cineverse_backend.global.util.cookie.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(
        name = "Auth",
        description = "인증 관리 API"
)
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    // 회원가입
    @Operation(
            summary = "회원가입",
            description = "회원가입"
    )
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO) {

        authService.signup(signupRequestDTO);

        return ResponseEntity.ok().build();
    }

    // 로그인
    @Operation(
            summary = "로그인",
            description = "로그인"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        LoginResultDTO result = authService.login(loginRequestDTO);

        // refreshToken 쿠키 생성
        ResponseCookie cookie = cookieUtil.createRefreshTokenCookie(result.getRefreshToken(), loginRequestDTO.getRememberMe());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString()) // http 응답 헤더에 쿠키 추가
                .body(result.getResponse());
    }

    // 로그아웃
    @Operation(
            summary = "로그아웃",
            description = "로그아웃"
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) { // 브라우저가 자동으로 보낸 쿠키 값을 가져옴 - 프론트에서 값을 던져주는게 아님

        // redis에 저장된 refreshToken 삭제
        authService.logout(refreshToken);

        // cookie에 저장된 refreshToken 삭제
        ResponseCookie cookie = cookieUtil.deleteRefreshTokenCookie();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    // 토큰 재발급
    @Operation(
            summary = "토큰 재발급",
            description = "토큰 재발급"
    )
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {

        // 토큰 재발급
        TokenResultDTO result = authService.refreshToken(refreshToken);

        // refreshToken 쿠키 생성
        ResponseCookie cookie = cookieUtil.createRefreshTokenCookie(result.getRefreshToken(), result.isRememberMe());


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result.getTokenResponseDTO());
    }
}
