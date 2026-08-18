package com.cineverse.cineverse_backend.domain.mail.dto;

import com.cineverse.cineverse_backend.domain.mail.enums.EmailVerificationType;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCodeSendRequestDTO {

    @Email
    private String email;

    private EmailVerificationType type;
}
