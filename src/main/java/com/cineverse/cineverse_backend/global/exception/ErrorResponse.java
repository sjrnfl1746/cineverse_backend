package com.cineverse.cineverse_backend.global.exception;

import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse { // 프론트로 내려줄 에러 응답 형태

    private final String code;
    private final String message;

    private ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // ErrorCode(enum)을 받아서 ErrorResponse로 변환
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.name(), errorCode.getMessage());
    }

    // enum에 없는 에러를 직접 만들때 사용
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message);
    }
}
