package com.cineverse.cineverse_backend.domain.content.dto.response;

import com.cineverse.cineverse_backend.domain.content.enums.ContentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentStatusResponseDTO { // 전체 콘텐츠 상태 반환

    private ContentStatus contentStatus; // 콘텐츠 상태

    private Long value; // 개수

    private Double percent; // 전체 비율 - (value / totalCount)
}
