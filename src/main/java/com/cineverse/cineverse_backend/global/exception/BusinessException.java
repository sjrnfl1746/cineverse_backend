package com.cineverse.cineverse_backend.global.exception;

import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException { // 직접 예외 클래스 구현

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
