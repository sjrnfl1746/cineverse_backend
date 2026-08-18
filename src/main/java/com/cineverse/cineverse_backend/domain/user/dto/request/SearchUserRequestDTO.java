package com.cineverse.cineverse_backend.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserRequestDTO {

    private String type; // 이름, 닉네임, 이메일

    private String keyword; // 키워드
}
