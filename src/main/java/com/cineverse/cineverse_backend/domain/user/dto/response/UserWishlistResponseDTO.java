package com.cineverse.cineverse_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWishlistResponseDTO {

    private Long userWishlistId;

    private Long contentId;

    private String contentTitle;

    private String posterUrl;

    private LocalDate releaseAt;
}
