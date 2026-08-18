package com.cineverse.cineverse_backend.domain.terms.controller;

import com.cineverse.cineverse_backend.domain.terms.dto.response.TermsResponseDTO;
import com.cineverse.cineverse_backend.domain.terms.service.TermsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms")
@Tag(
        name = "Terms",
        description = "약관 API"
)
public class TermsController {

    private final TermsService termsService;

    @GetMapping("/active")
    @Operation(
            summary = "회원가입 약관 조회",
            description = "회원가입 약관 조회"
    )
    public ResponseEntity<List<TermsResponseDTO>> getActiveTerms() {
        return ResponseEntity.ok(termsService.getActiveTerms());
    }
}
