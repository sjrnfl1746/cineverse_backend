package com.cineverse.cineverse_backend.domain.user.dto.request;

import com.cineverse.cineverse_backend.domain.user.enums.UserRole;
import com.cineverse.cineverse_backend.domain.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyRequestDTO {

    private UserStatus status;

    private UserRole role;
}
