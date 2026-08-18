package com.cineverse.cineverse_backend.domain.auth.service;

import com.cineverse.cineverse_backend.domain.auth.dto.request.LoginRequestDTO;
import com.cineverse.cineverse_backend.domain.auth.dto.request.SignupRequestDTO;
import com.cineverse.cineverse_backend.domain.auth.dto.response.LoginResultDTO;
import com.cineverse.cineverse_backend.domain.auth.dto.response.TokenResultDTO;

public interface AuthService {

    // 회원가입
    void signup(SignupRequestDTO signupRequestDTO);

    // 로그인
    LoginResultDTO login(LoginRequestDTO loginRequestDTO);

    // 로그아웃
    void logout(String refreshToken);

    // 토큰 재발급
    TokenResultDTO refreshToken(String refreshToken);
}
