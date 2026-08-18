package com.cineverse.cineverse_backend.domain.auth.dto.response;

import com.cineverse.cineverse_backend.domain.user.dto.response.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    // token
    private String accessToken;

    // 구독 여부
    private boolean subscribed;

    // user 부분
    private UserResponseDTO user;
}
