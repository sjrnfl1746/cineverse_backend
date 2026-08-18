package com.cineverse.cineverse_backend.domain.subscription.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderResponseDTO {

    private String orderId;

    private String orderName;

    private Long amount;

    private String currency;

    private String customerKey;
}
