package com.cineverse.cineverse_backend.domain.subscription.controller;

import com.cineverse.cineverse_backend.domain.subscription.dto.request.PaymentPrepareRequestDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.PaymentOrderResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.PaymentResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.service.payment.PaymentService;
import com.cineverse.cineverse_backend.global.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@Tag(name = "Payment", description = "결제 관련 API")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 준비", description = "결제 준비")
    @PostMapping("/prepare")
    public ResponseEntity<PaymentResponseDTO> preparePayment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @Valid @RequestBody PaymentPrepareRequestDTO paymentPrepareRequestDTO) {
        return ResponseEntity.ok(paymentService.preparePayment(userDetails.getUserId(), paymentPrepareRequestDTO.getSubscriptionPlanId()));
    }

    @Operation(summary = "결제 내역 조회", description = "orderId로 결제 내역 조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentOrderResponseDTO> getPayment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                              @PathVariable String orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId, userDetails.getUserId()));
    }
}
