package com.cineverse.cineverse_backend.domain.content.service.genre;

import com.cineverse.cineverse_backend.domain.content.dto.response.GenreResponseDTO;

import java.util.List;

public interface GenreService {

    // 장르 리스트 조회
    List<GenreResponseDTO> getAllGenres();
}
