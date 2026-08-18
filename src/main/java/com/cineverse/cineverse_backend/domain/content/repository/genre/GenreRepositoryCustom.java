package com.cineverse.cineverse_backend.domain.content.repository.genre;

import com.cineverse.cineverse_backend.domain.content.dto.response.GenreResponseDTO;

import java.util.List;

public interface GenreRepositoryCustom {

    List<GenreResponseDTO> findGenresByContentId(Long contentId);
}
