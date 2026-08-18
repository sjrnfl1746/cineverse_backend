package com.cineverse.cineverse_backend.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO {

    @NotBlank
    private String zipCode;

    @NotBlank
    private String city;

    @NotBlank
    private String district;

    @NotBlank
    private String street;

    private String detail;
}
