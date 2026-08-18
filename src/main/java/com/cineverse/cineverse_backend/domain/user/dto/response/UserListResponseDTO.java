package com.cineverse.cineverse_backend.domain.user.dto.response;

import com.cineverse.cineverse_backend.domain.user.enums.Gender;
import com.cineverse.cineverse_backend.domain.user.enums.UserRole;
import com.cineverse.cineverse_backend.domain.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponseDTO {

    private Long userId;

    private String email;

    private String nickname;

    private String name;

    private Gender gender;

    private LocalDate birthDate;

    private String phoneNumber;

    private UserRole role;

    private UserStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
