package com.cineverse.cineverse_backend.domain.subscription.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanResponseDTO {

    private Long subscriptionPlanId;

    private String code;

    private String name;

    private Long amount;

    private Integer billingCycleMonths;

    private boolean subscribed; // 구독 여부

    private LocalDateTime currentPeriodEndAt; // 현재 구독중인 플랜 만료일
}
