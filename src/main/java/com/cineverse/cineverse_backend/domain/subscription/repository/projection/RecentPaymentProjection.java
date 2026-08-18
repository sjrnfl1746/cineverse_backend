package com.cineverse.cineverse_backend.domain.subscription.repository.projection;

import java.time.LocalDateTime;

public interface RecentPaymentProjection {

    Long getPaymentId();

    String getName();

    String getPlan();

    Long getAmount();

    LocalDateTime getPaidAt();
}
