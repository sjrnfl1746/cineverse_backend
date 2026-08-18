package com.cineverse.cineverse_backend.domain.auth.dto.request;

import com.cineverse.cineverse_backend.domain.user.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    private String name;

    private Gender gender;

    @NotNull
    private LocalDate birthDate;

    @NotBlank
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$")
    private String phoneNumber;

    // 주소
    @Valid
    @NotNull
    private AddressRequestDTO address;

    // 약관동의 id
    @NotEmpty
    private List<Long> agreedTermsIds;
}