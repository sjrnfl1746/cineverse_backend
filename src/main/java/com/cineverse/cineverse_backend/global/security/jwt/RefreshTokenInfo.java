package com.cineverse.cineverse_backend.global.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenInfo {

    private Long userId;

    private boolean rememberMe;
}
