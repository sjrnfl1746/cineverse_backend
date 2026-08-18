package com.cineverse.cineverse_backend.domain.dashboard.service;

import com.cineverse.cineverse_backend.domain.dashboard.dto.DashboardResponseDTO;

public interface DashboardService {

    // 대시보드 값들 받아오는 메서드
    DashboardResponseDTO getAllDashboards();
}
