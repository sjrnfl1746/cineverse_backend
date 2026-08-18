package com.cineverse.cineverse_backend.domain.content.repository.genre;

import com.cineverse.cineverse_backend.domain.content.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long>, GenreRepositoryCustom {

    // genreId로 genre 조회
    Genre findByGenreId(Long genreId);
}
