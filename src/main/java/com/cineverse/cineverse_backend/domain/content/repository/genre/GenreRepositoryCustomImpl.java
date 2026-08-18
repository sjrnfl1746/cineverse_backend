package com.cineverse.cineverse_backend.domain.content.repository.genre;

import com.cineverse.cineverse_backend.domain.content.dto.response.GenreResponseDTO;
import com.cineverse.cineverse_backend.domain.content.entity.QContentGenre;
import com.cineverse.cineverse_backend.domain.content.entity.QGenre;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QGenre genre = QGenre.genre;
    private final QContentGenre contentGenre = QContentGenre.contentGenre;

    @Override
    public List<GenreResponseDTO> findGenresByContentId(Long contentId) {
        return queryFactory
                .select(Projections.fields(
                        GenreResponseDTO.class,
                        genre.genreId,
                        genre.genreCode,
                        genre.genreName,
                        genre.sortOrder
                ))
                .from(contentGenre)
                .join(genre)
                .on(contentGenre.genre.genreId.eq(genre.genreId))
                .where(contentGenre.content.contentId.eq(contentId))
                .fetch();
    }
}
