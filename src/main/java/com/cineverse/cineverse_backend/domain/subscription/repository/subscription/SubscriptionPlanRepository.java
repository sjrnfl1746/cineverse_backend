package com.cineverse.cineverse_backend.domain.subscription.repository.subscription;

import com.cineverse.cineverse_backend.domain.subscription.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
}
