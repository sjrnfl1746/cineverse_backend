package com.cineverse.cineverse_backend.domain.dashboard.controller;

import com.cineverse.cineverse_backend.domain.dashboard.dto.DashboardResponseDTO;
import com.cineverse.cineverse_backend.domain.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/dashboard")
@Tag(name = "Admin Dashboard", description = "관리자 대시보드 API")
public class DashboardAdminController {

    private final DashboardService dashboardService;

    @Operation(summary = "대시보드 정보 반환", description = "대시보드 관련 정보들을 반환")
    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard() {
        return ResponseEntity.ok(dashboardService.getAllDashboards());
    }
}
