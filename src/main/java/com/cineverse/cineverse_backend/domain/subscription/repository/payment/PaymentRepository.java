package com.cineverse.cineverse_backend.domain.subscription.repository.payment;

import com.cineverse.cineverse_backend.domain.subscription.entity.Payment;
import com.cineverse.cineverse_backend.domain.subscription.enums.PaymentStatus;
import com.cineverse.cineverse_backend.domain.subscription.repository.projection.MonthlySalesProjection;
import com.cineverse.cineverse_backend.domain.subscription.repository.projection.RecentPaymentProjection;
import com.cineverse.cineverse_backend.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // 기존 결제 내역 조회
    Payment findByUser_UserIdAndSubscriptionPlan_SubscriptionPlanIdAndStatus(Long userId, Long subscriptionPlanId, PaymentStatus status);

    // orderId로 주문 내역 조회
    Optional<Payment> findByOrderId(String orderId);

    // 기간내 합계 금액 확인
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0) FROM Payment p
                        WHERE p.status = :status AND p.createdAt >= :start AND p.createdAt < :end
            """)
    Long sumAmountByStatusAndCreatedAtBetween(@Param("status") PaymentStatus status, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Payment> user(User user);

    // 해당하는 달의 합계 금액
    @Query("""
            SELECT MONTH(p.createdAt) AS month, SUM(p.amount) AS amount FROM Payment p
                        WHERE p.status = :status AND p.createdAt >= :start AND p.createdAt < :end
                                    GROUP BY MONTH(p.createdAt) ORDER BY MONTH(p.createdAt)
            """)
    List<MonthlySalesProjection> findMonthlySales(@Param("status") PaymentStatus status, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 최근 결제 내역조회 - 4개
    @Query("""
            SELECT p.paymentId AS paymentId, u.name AS name, sp.name AS plan, p.amount AS amount, p.createdAt AS paidAt FROM Payment p
                        JOIN p.subscription s
                                    JOIN s.user u
                                                JOIN s.subscriptionPlan sp
                                                            WHERE p.status = :status
                                                                        ORDER BY p.createdAt DESC
                                                                                    limit 4
            """)
    List<RecentPaymentProjection> findRecentPaymentTop4(@Param("status") PaymentStatus status);
}
