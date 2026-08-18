package com.cineverse.cineverse_backend.domain.subscription.service.subscription;

import com.cineverse.cineverse_backend.domain.subscription.dto.response.SubscriptionPlanResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.entity.Subscription;
import com.cineverse.cineverse_backend.domain.subscription.entity.SubscriptionPlan;
import com.cineverse.cineverse_backend.domain.subscription.enums.SubscriptionStatus;
import com.cineverse.cineverse_backend.domain.subscription.repository.subscription.SubscriptionPlanRepository;
import com.cineverse.cineverse_backend.domain.subscription.repository.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Transactional
    @Override
    public List<SubscriptionPlanResponseDTO> getAllSubscriptionPlan(Long userId) {

        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();

        List<SubscriptionPlanResponseDTO> subscriptionPlanResponseDTOList = new ArrayList<>();

        // 전체 플랜들에 해당하는 값들과 구독 여부 및 만료일 저장
        for (SubscriptionPlan subscriptionPlan : subscriptionPlanList) {

            SubscriptionPlanResponseDTO subscriptionPlanResponseDTO = SubscriptionPlanResponseDTO.builder()
                    .subscriptionPlanId(subscriptionPlan.getSubscriptionPlanId())
                    .code(subscriptionPlan.getCode())
                    .name(subscriptionPlan.getName())
                    .amount(subscriptionPlan.getAmount())
                    .billingCycleMonths(subscriptionPlan.getBillingCycleMonths())
                    .build();

            // 구독 조회
            Subscription subscription = subscriptionRepository.findByUser_UserIdAndSubscriptionPlan_SubscriptionPlanIdAndStatus(userId,
                    subscriptionPlan.getSubscriptionPlanId(), SubscriptionStatus.ACTIVE).orElse(null);

            if (subscription != null) {
                subscriptionPlanResponseDTO.setSubscribed(true);
                subscriptionPlanResponseDTO.setCurrentPeriodEndAt(subscription.getStartedAt().plusMonths(subscriptionPlan.getBillingCycleMonths()));
            } else {
                subscriptionPlanResponseDTO.setSubscribed(false);
            }

            subscriptionPlanResponseDTOList.add(subscriptionPlanResponseDTO);
        }

        return subscriptionPlanResponseDTOList;
    }

    @Transactional
    @Override
    public int updateSubscriptionStatuses() {

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        return subscriptionRepository.expireSubscriptions(now, SubscriptionStatus.ACTIVE, SubscriptionStatus.EXPIRED);
    }
}
