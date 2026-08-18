package com.cineverse.cineverse_backend.domain.content.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentTopViewResponseDTO { // 최고 조회수 top 5

    private Long contentId;

    private String title;

    private Long viewCnt;
}
