package com.cineverse.cineverse_backend.domain.content.dto.response;

import com.cineverse.cineverse_backend.domain.content.enums.ContentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentDashboardResponseDTO {

    private Long contentId;

    private String title;

    private ContentStatus contentStatus;

    private LocalDateTime createdAt;
}
