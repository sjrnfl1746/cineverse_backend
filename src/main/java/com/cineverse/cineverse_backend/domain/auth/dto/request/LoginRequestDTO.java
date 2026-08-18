package com.cineverse.cineverse_backend.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private Boolean rememberMe;
}
