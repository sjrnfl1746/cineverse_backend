package com.cineverse.cineverse_backend.domain.subscription.controller;

import com.cineverse.cineverse_backend.domain.subscription.dto.response.SubscriptionPlanResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.service.subscription.SubscriptionService;
import com.cineverse.cineverse_backend.global.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
@Tag(name = "Subscription", description = "구독 관련 API")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(summary = "구독 플랜들 조회", description = "구독 플랜 리스트 반환")
    @GetMapping
    public ResponseEntity<List<SubscriptionPlanResponseDTO>> getAllSubscriptionPlans(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptionPlan(userDetails.getUserId()));
    }
}
