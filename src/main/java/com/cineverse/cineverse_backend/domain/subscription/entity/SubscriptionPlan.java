package com.cineverse.cineverse_backend.domain.subscription.entity;

import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "subscription_plan")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionPlan extends BaseEntity { // 판매할 구독 상품 정보 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionPlanId;

    private String code; // 구분하는 코드 - name 값이 바뀌어도 code는 유지됨

    private String name; // 구독 상품명

    private Long amount; // 결제 금액

    private Integer billingCycleMonths; // 몇 개월 마다 결제 할지

    private boolean active; // 현재 판매중인지
}
