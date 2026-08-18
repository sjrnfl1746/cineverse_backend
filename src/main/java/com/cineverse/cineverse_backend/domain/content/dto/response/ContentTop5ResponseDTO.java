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
public class ContentTop5ResponseDTO {

    private Long contentId;

    private String title;

    private String thumbnailUrl;

    private List<GenreResponseDTO> genres;
}
