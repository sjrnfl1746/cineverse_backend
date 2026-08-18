package com.cineverse.cineverse_backend.global.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode { // 에러 종류

    // 에러코드 추가
    INVALID_EMAIL_CODE(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
    EXPIRED_EMAIL_CODE(HttpStatus.BAD_REQUEST, "인증번호가 만료되었거나 존재하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용중인 닉네임입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "가입되지 않은 이메일입니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "이메일이 인증되지 않았거나 만료되었습니다."),
    REQUIRED_TERMS_NOT_AGREED(HttpStatus.BAD_REQUEST, "필수 약관에 동의해야 합니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
