package com.cineverse.cineverse_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressResponseDTO {

    private Long addressId;

    private String zipCode;

    private String city;

    private String district;

    private String street;

    private String detail;
}
