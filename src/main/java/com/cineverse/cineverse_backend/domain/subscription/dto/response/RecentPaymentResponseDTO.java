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
public class RecentPaymentResponseDTO {

    private Long paymentId;

    private String name; // 사용자

    private String plan; // 플랜명

    private Long amount; // 금액

    private LocalDateTime paidAt; // 결제시간
}
