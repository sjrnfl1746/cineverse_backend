package com.cineverse.cineverse_backend.domain.event.enums;

public enum EventStatus {
    READY, // 진행 예정
    PUBLISHED, // 진행 중
    RESULT_PENDING, // 결과 대기
    END, // 종료
}
