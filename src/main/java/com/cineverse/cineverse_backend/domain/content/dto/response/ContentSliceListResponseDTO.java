package com.cineverse.cineverse_backend.domain.content.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentSliceListResponseDTO {

    private Long contentId;

    private String title;

    private List<GenreResponseDTO> genres;

    private String thumbnailUrl;
}
