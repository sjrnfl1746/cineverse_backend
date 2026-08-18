package com.cineverse.cineverse_backend.global.exception;

import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler { // 예외를 잡아서 JSON 응답으로 변환

    // 직접만든 비즈니스 예외
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.from(errorCode));
    }

    // @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        return ResponseEntity.badRequest().body(ErrorResponse.of("VALIDATION_ERROR", message));
    }

    // IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of("BAD_REQUEST", e.getMessage()));
    }
}
