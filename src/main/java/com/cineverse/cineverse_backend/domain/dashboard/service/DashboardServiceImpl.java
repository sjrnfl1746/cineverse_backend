package com.cineverse.cineverse_backend.domain.dashboard.service;

import com.cineverse.cineverse_backend.domain.content.dto.response.ContentDashboardResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentStatusResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentTopViewResponseDTO;
import com.cineverse.cineverse_backend.domain.content.service.content.ContentService;
import com.cineverse.cineverse_backend.domain.dashboard.dto.DashboardResponseDTO;
import com.cineverse.cineverse_backend.domain.dashboard.dto.SummaryDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.MonthlySalesResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.RecentPaymentResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.service.payment.PaymentService;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserDashboardResponseDTO;
import com.cineverse.cineverse_backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ContentService contentService;
    private final UserService userService;
    private final PaymentService paymentService;

    @Transactional
    @Override
    public DashboardResponseDTO getAllDashboards() {

        /***** 대시보드 요약본 *****/
        List<SummaryDTO> summaryList = new ArrayList<>();

        // 전체 콘텐츠 수
        SummaryDTO totalContentCount = contentService.getAllContentCount();
        summaryList.add(totalContentCount);

        // 전체 회원 수
        SummaryDTO totalUserCount = userService.getAllUserCount();
        summaryList.add(totalUserCount);

        // 구독 회원 수
        SummaryDTO totalSubscribedUserCount = userService.countSubscribedUsers();
        summaryList.add(totalSubscribedUserCount);

        // 이번 달 매출
        SummaryDTO totalPaymentInMonth = paymentService.getSumAmountByStatusAndCreatedAtBetween();
        summaryList.add(totalPaymentInMonth);

        /***** 해당년도의 월별 매출 *****/
        int year = LocalDate.now().getYear(); // 해당년도 조회
        List<MonthlySalesResponseDTO> monthlySalesResponseDTOList = paymentService.getMonthlySales(year);

        /***** 최고 시청 영화 top5 *****/
        List<ContentTopViewResponseDTO> contentTopViewResponseDTOList = contentService.getAllContentTopView();

        /***** 콘텐츠 현황 *****/
        List<ContentStatusResponseDTO> contentStatusResponseDTOList = contentService.getAllContentStatus();

        /***** 최근 등록 콘텐츠 - 4개 *****/
        List<ContentDashboardResponseDTO> contentDashboardResponseDTOList = contentService.getAllContentDashboard();

        /***** 최근 가입 회원 - 4명 *****/
        List<UserDashboardResponseDTO> userDashboardResponseDTOList = userService.getAllUserDashboard();

        /***** 최근 결제 내역 - 4개 *****/
        List<RecentPaymentResponseDTO> recentPaymentResponseDTOList = paymentService.getRecentPayments();

        return DashboardResponseDTO.builder()
                .summaryList(summaryList)
                .monthlySalesList(monthlySalesResponseDTOList)
                .contentTopViewList(contentTopViewResponseDTOList)
                .contentStatusList(contentStatusResponseDTOList)
                .contentList(contentDashboardResponseDTOList)
                .userList(userDashboardResponseDTOList)
                .recentPaymentList(recentPaymentResponseDTOList)
                .build();
    }
}
