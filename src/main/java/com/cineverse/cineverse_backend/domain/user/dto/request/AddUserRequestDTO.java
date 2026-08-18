package com.cineverse.cineverse_backend.domain.user.dto.request;

import com.cineverse.cineverse_backend.domain.user.enums.Gender;
import com.cineverse.cineverse_backend.domain.user.enums.UserRole;
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
public class AddUserRequestDTO {

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    private String name;

    private Gender gender = Gender.NONE;

    private String phoneNumber = "010-0000-0000";

    private UserRole role;
}
