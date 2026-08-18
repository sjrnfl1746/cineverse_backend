package com.cineverse.cineverse_backend.domain.subscription.entity;

import com.cineverse.cineverse_backend.domain.subscription.enums.SubscriptionStatus;
import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(name = "subscription")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Subscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // fk

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlan subscriptionPlan; // fk

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status; // 구독 상태

    private LocalDateTime startedAt; // 구독 시작일

    private boolean autoRenew; // 자동 갱신 여부

    private LocalDateTime nextPaymentAt; // 다음 자동 결제일 - autoRenew가 true인 경우
}
