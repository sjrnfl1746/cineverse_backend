package com.cineverse.cineverse_backend.domain.subscription.service.payment;

import com.cineverse.cineverse_backend.domain.dashboard.dto.SummaryDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.MonthlySalesResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.PaymentOrderResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.PaymentResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.RecentPaymentResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.entity.Payment;
import com.cineverse.cineverse_backend.domain.subscription.entity.Subscription;
import com.cineverse.cineverse_backend.domain.subscription.entity.SubscriptionPlan;
import com.cineverse.cineverse_backend.domain.subscription.enums.PaymentProvider;
import com.cineverse.cineverse_backend.domain.subscription.enums.PaymentStatus;
import com.cineverse.cineverse_backend.domain.subscription.enums.SubscriptionStatus;
import com.cineverse.cineverse_backend.domain.subscription.repository.payment.PaymentRepository;
import com.cineverse.cineverse_backend.domain.subscription.repository.projection.MonthlySalesProjection;
import com.cineverse.cineverse_backend.domain.subscription.repository.subscription.SubscriptionPlanRepository;
import com.cineverse.cineverse_backend.domain.subscription.repository.subscription.SubscriptionRepository;
import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.domain.user.repository.UserRepository;
import com.cineverse.cineverse_backend.global.exception.BusinessException;
import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Transactional
    @Override
    public PaymentResponseDTO preparePayment(Long userId, Long subscriptionPlanId) {

        // user 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // subscriptionPlan 조회
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(subscriptionPlanId).orElseThrow(
                () -> new RuntimeException("구독 플랜이 존재하지 않습니다.")
        );

        // 기존 결제 준비 내역 있는지 조회
        Payment existPayment = paymentRepository.findByUser_UserIdAndSubscriptionPlan_SubscriptionPlanIdAndStatus(userId, subscriptionPlanId, PaymentStatus.READY);

        if (existPayment != null) {
            return PaymentResponseDTO.builder()
                    .orderId(existPayment.getOrderId())
                    .build();
        }

        // orderId
        String orderId = "CINEVERSE_" + UUID.randomUUID();

        Payment payment = Payment.builder()
                .user(user)
                .subscriptionPlan(subscriptionPlan)
                .orderId(orderId)
                .orderName(subscriptionPlan.getName())
                .amount(subscriptionPlan.getAmount())
                .status(PaymentStatus.READY)
                .provider(PaymentProvider.TOSS)
                .requestedAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return PaymentResponseDTO.builder()
                .orderId(orderId)
                .build();
    }

    @Transactional
    @Override
    public PaymentOrderResponseDTO getPaymentByOrderId(String orderId, Long userId) {

        // 결제 내역 조회
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("주문 내역이 존재 하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 결제 상태 확인 - status가 ready가 아니라면
        if (payment.getStatus() != PaymentStatus.READY) {
            throw new RuntimeException("결제 준비중인 주문이 아닙니다.");
        }

        // userId가 일치하지 않는 경우
        if (!user.getUserId().equals(userId)) {
            throw new RuntimeException("접근 권한이 없는 계정입니다.");
        }

        return PaymentOrderResponseDTO.builder()
                .orderId(orderId)
                .orderName(payment.getOrderName())
                .amount(payment.getAmount())
                .currency("KRW")
                .customerKey(user.getCustomerKey())
                .build();
    }

    @Transactional
    @Override
    public void approvePayment(String orderId, String paymentKey, String method, LocalDateTime approvedAt) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        // 구독 여부 저장
        Subscription subscription = Subscription.builder()
                .user(payment.getUser())
                .subscriptionPlan(payment.getSubscriptionPlan())
                .status(SubscriptionStatus.ACTIVE)
                .startedAt(LocalDateTime.now())
                .build();
        subscriptionRepository.save(subscription);

        // user 엔티티의 구독 여부 true로 변경
        payment.getUser().changeSubscribe();

        // 결제 저장
        payment.approve(paymentKey, method, subscription, approvedAt);
    }

    @Transactional
    @Override
    public void failPayment(String orderId, String failureCode, String failureMessage) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new RuntimeException("결제 정보를 찾을 수 없습니다."));
        payment.fail(failureCode, failureMessage, LocalDateTime.now());
    }

    @Transactional
    @Override
    public SummaryDTO getSumAmountByStatusAndCreatedAtBetween() {

        LocalDate today = LocalDate.now();

        // 시작일
        LocalDateTime start = today.withDayOfMonth(1).atStartOfDay();

        // 종료일
        LocalDateTime end = start.plusMonths(1);

        return SummaryDTO.builder()
                .title("이번 달 매출")
                .value(paymentRepository.sumAmountByStatusAndCreatedAtBetween(PaymentStatus.DONE, start, end))
                .money(true)
                .build();
    }

    @Transactional
    @Override
    public List<MonthlySalesResponseDTO> getMonthlySales(int year) {

        // 시작일
        LocalDateTime start = LocalDate.of(year, 1, 1).atStartOfDay();

        // 종료일
        LocalDateTime end = start.plusYears(1);

        // 월별 매출 조회
        List<MonthlySalesProjection> sales = paymentRepository.findMonthlySales(PaymentStatus.DONE, start, end);

        // 월 - 매출 형태로 변환
        Map<Integer, Long> salesByMonth = new HashMap<>();

        for (MonthlySalesProjection sale : sales) {
            salesByMonth.put(sale.getMonth(), sale.getAmount());
        }

        // 1 ~ 12월까지 생성
        List<MonthlySalesResponseDTO> result = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            result.add(MonthlySalesResponseDTO.builder()
                    .month(month + "월")
                    .amount(salesByMonth.getOrDefault(month, 0L))
                    .build());
        }

        return result;
    }

    @Override
    public List<RecentPaymentResponseDTO> getRecentPayments() {
        return paymentRepository.findRecentPaymentTop4(PaymentStatus.DONE)
                .stream().map(payment -> RecentPaymentResponseDTO.builder()
                        .paymentId(payment.getPaymentId())
                        .name(payment.getName())
                        .plan(payment.getPlan())
                        .amount(payment.getAmount())
                        .paidAt(payment.getPaidAt())
                        .build())
                .toList();
    }
}
