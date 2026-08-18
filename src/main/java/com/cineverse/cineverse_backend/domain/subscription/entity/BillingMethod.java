package com.cineverse.cineverse_backend.domain.subscription.entity;

import com.cineverse.cineverse_backend.domain.subscription.enums.PaymentProvider;
import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(name = "billing_method")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BillingMethod extends BaseEntity { // 자동결제에 사용할 등록된 카드 및 빌링키 저장

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billingMethodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String customerKey; // 사용자 식별 키 - UUID를 사용해 랜덤 식별자 사용

    private String billingKey;

    @Enumerated(EnumType.STRING)
    private PaymentProvider provider; // 결제 대행사 - TOSS 사용

    private String cardCompany; // 카드사 이름

    private String cardNumber; // 마스킹된 카드 번호

    private String cardType; // 신용카드, 체크카드 등의 구분

    private boolean active; // 현재 사용할지 여부

    private LocalDateTime registeredAt; // 카드 등록일

    private LocalDateTime deactivatedAt; // 비활성화 시점
}
