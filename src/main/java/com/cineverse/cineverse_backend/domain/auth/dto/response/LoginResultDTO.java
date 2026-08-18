package com.cineverse.cineverse_backend.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResultDTO {

    private String refreshToken;

    private LoginResponseDTO response;
}
