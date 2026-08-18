package com.cineverse.cineverse_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponseDTO {

    private String title;

    private Long value;
}
