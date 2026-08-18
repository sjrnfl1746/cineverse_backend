package com.cineverse.cineverse_backend.domain.content.repository;

import com.cineverse.cineverse_backend.domain.content.entity.ContentGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentGenreRepository extends JpaRepository<ContentGenre, Long> {

    // contentId로 content, genre 매핑 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            DELETE FROM ContentGenre cg WHERE cg.content.contentId = :contentId
            """)
    void deleteAllByContentId(@Param("contentId") Long contentId);
}
