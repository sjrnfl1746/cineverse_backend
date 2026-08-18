package com.cineverse.cineverse_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDashboardResponseDTO {

    private Long userId;

    private String name;

    private String email;

    private LocalDateTime createdAt;
}
