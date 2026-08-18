package com.cineverse.cineverse_backend.domain.subscription.service.payment;

import com.cineverse.cineverse_backend.domain.dashboard.dto.SummaryDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.request.PaymentPrepareRequestDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.MonthlySalesResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.PaymentOrderResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.PaymentResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.RecentPaymentResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {

    // 결제 준비
    PaymentResponseDTO preparePayment(Long userId, Long subscriptionPlanId);

    // orderId로 결제 내역 조회
    PaymentOrderResponseDTO getPaymentByOrderId(String orderId, Long userId);

    // 결제 승인
    void approvePayment(String orderId, String paymentKey, String method, LocalDateTime approvedAt);

    // 결제 실패
    void failPayment(String orderId, String failureCode, String failureMessage);

    // 기간 내 결제 금액 조회
    SummaryDTO getSumAmountByStatusAndCreatedAtBetween();

    // 해당년도 월별 매출 조회
    List<MonthlySalesResponseDTO> getMonthlySales(int year);

    // 최근 결제 내역 4개 조회
    List<RecentPaymentResponseDTO> getRecentPayments();
}
