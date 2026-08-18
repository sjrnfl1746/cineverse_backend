package com.cineverse.cineverse_backend.domain.subscription.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySalesResponseDTO {

    private String month;

    private Long amount;
}
