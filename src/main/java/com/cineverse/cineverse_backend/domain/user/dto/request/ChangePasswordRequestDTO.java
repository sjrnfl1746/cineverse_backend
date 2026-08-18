package com.cineverse.cineverse_backend.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequestDTO {

    private String currentPassword;

    private String newPassword;
}
