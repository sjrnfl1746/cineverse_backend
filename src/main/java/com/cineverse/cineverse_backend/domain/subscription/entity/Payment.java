package com.cineverse.cineverse_backend.domain.subscription.entity;

import com.cineverse.cineverse_backend.domain.subscription.enums.PaymentProvider;
import com.cineverse.cineverse_backend.domain.subscription.enums.PaymentStatus;
import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlan subscriptionPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_method_id")
    private BillingMethod billingMethod;

    private String orderId; // 주문번호

    private String paymentKey; // 토스가 생성한 결제 식별키

    private String orderName; // 주문명

    private Long amount; // 결제 금액

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // 결제 준비, 성공, 실패, 취소 상태값

    @Enumerated(EnumType.STRING)
    private PaymentProvider provider; // 결제 대행사

    private String method; // 결제 방식

    private String failureCode; // 결제 실패 코드

    private String failureMessage; // 결제 실패 사유

    private LocalDateTime requestedAt; // 결제 요청 시간

    private LocalDateTime approvedAt; // 결제 승인 시간

    private LocalDateTime failedAt; // 결제 실패 시간

    private LocalDateTime canceledAt; // 결제 취소 시간

    // 결제 승인
    public void approve(String paymentKey, String method, Subscription subscription, LocalDateTime approvedAt) {
        this.paymentKey = paymentKey;
        this.method = method;
        this.approvedAt = approvedAt;
        this.subscription = subscription;
        this.status = PaymentStatus.DONE;
    }

    // 결제 실패
    public void fail(String failureCode, String failureMessage, LocalDateTime failedAt) {
        this.failureCode = failureCode;
        this.failureMessage = failureMessage;
        this.failedAt = failedAt;
        this.status = PaymentStatus.FAILED;
    }
}
