package com.cineverse.cineverse_backend.domain.subscription.repository.projection;

public interface MonthlySalesProjection { // 월, 월별 합계 금액 받을 projection

    Integer getMonth();

    Long getAmount();
}
