package com.cineverse.cineverse_backend.domain.dashboard.dto;

import com.cineverse.cineverse_backend.domain.content.dto.response.ContentDashboardResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentStatusResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentTopViewResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.MonthlySalesResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.RecentPaymentResponseDTO;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserDashboardResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {

    // 대시보드 요약본
    List<SummaryDTO> summaryList;

    // 해당년도 월별 매출
    List<MonthlySalesResponseDTO> monthlySalesList;

    // 최고 조회수 top 5
    List<ContentTopViewResponseDTO> contentTopViewList;

    // 콘텐츠 현황
    List<ContentStatusResponseDTO> contentStatusList;

    // 최근 등록한 4개 콘텐츠
    List<ContentDashboardResponseDTO> contentList;

    // 최근 가입한 4명의 회원
    List<UserDashboardResponseDTO> userList;

    // 최근 결제 내역 4개
    List<RecentPaymentResponseDTO> recentPaymentList;
}
