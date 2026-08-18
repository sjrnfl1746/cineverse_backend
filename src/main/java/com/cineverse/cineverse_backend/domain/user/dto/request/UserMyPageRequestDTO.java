package com.cineverse.cineverse_backend.domain.user.dto.request;

import com.cineverse.cineverse_backend.domain.user.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMyPageRequestDTO {

    private String nickname;

    private String name;

    private Gender gender;

    private LocalDate birthDate;

    private String phoneNumber;

    private String zipCode;

    private String city;

    private String district;

    private String street;

    private String detail;
}
