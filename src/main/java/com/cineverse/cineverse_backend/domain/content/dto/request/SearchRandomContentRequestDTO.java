package com.cineverse.cineverse_backend.domain.content.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRandomContentRequestDTO {

    private Long seed; // 초기 시드값

    private String type;

    private String keyword;
}
