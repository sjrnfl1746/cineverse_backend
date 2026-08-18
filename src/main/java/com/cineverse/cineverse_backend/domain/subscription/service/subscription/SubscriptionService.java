package com.cineverse.cineverse_backend.domain.subscription.service.subscription;

import com.cineverse.cineverse_backend.domain.subscription.dto.response.SubscriptionPlanResponseDTO;

import java.util.List;

public interface SubscriptionService {

    // 구독 플랜들 반환
    List<SubscriptionPlanResponseDTO> getAllSubscriptionPlan(Long userId);

    // 구독 상태 수정
    int updateSubscriptionStatuses();
}
