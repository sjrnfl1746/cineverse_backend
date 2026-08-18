package com.cineverse.cineverse_backend.domain.content.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreResponseDTO {

    private Long genreId;

    private String genreCode;

    private String genreName;

    private Long sortOrder;
}
