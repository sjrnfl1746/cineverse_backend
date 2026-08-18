package com.cineverse.cineverse_backend.domain.content.service.genre;

import com.cineverse.cineverse_backend.domain.content.dto.response.GenreResponseDTO;
import com.cineverse.cineverse_backend.domain.content.entity.Genre;
import com.cineverse.cineverse_backend.domain.content.repository.genre.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<GenreResponseDTO> getAllGenres() {

        // 장르 리스트 조회
        List<Genre> genres = genreRepository.findAll();

        List<GenreResponseDTO> genreResponseDTOS = new ArrayList<>();

        for (Genre genre : genres) {
            GenreResponseDTO dto = GenreResponseDTO.builder()
                    .genreId(genre.getGenreId())
                    .genreCode(genre.getGenreCode())
                    .genreName(genre.getGenreName())
                    .sortOrder(genre.getSortOrder())
                    .build();
            genreResponseDTOS.add(dto);
        }

        return genreResponseDTOS;
    }
}
