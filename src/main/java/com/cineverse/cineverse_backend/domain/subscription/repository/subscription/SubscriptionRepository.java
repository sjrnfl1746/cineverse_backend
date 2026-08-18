package com.cineverse.cineverse_backend.domain.subscription.repository.subscription;

import com.cineverse.cineverse_backend.domain.subscription.entity.Subscription;
import com.cineverse.cineverse_backend.domain.subscription.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // 구독 조회
    Optional<Subscription> findByUser_UserIdAndSubscriptionPlan_SubscriptionPlanIdAndStatus(Long userId, Long subscriptionPlanId, SubscriptionStatus status);

    // 구독중인 플랜 조회
    Subscription findByUser_UserIdAndStatus(Long userId, SubscriptionStatus status);

    // 구독 상태 만료 - ACTIVE - EXPIRED
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            UPDATE subscription s INNER JOIN subscription_plan sp ON sp.subscription_plan_id = s.subscription_plan_id
               SET s.status = :expiredStatus WHERE s.status = :activeStatus AND s.started_at IS NOT NULL AND DATE_ADD(s.started_at, INTERVAL sp.billing_cycle_months MONTH) <= :now
            """, nativeQuery = true)
    int expireSubscriptions(@Param("now") LocalDateTime now, @Param("activeStatus") SubscriptionStatus activeStatus, @Param("expiredStatus") SubscriptionStatus expiredStatus);
}
