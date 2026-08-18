package com.cineverse.cineverse_backend.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDTO { // 대시보드 요약본 - 전체 콘텐츠, 전체 회원, 구독회원, 이번 달 매출

    private String title; // 제목

    private Long value; // 값

    private boolean money; // 금액 관련 요약 여부
}
