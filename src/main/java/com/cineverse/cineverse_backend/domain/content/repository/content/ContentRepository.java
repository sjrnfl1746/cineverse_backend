package com.cineverse.cineverse_backend.domain.content.repository.content;

import com.cineverse.cineverse_backend.domain.content.entity.Content;
import com.cineverse.cineverse_backend.domain.content.enums.ContentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long>, ContentRepositoryCustom {

    // deleted가 false인 콘텐츠의 개수
    Long countByDeletedFalse();

    // 최근 등록한 콘텐츠 4개 반환
    List<Content> findTop4ByDeletedFalseOrderByCreatedAtDesc();

    // top 5 조회수 콘텐츠 반환
    List<Content> findTop5ByDeletedFalseOrderByViewCntDescCreatedAtDesc();

    // contentStatus로 콘텐츠 개수 조회
    Long countByContentStatusAndDeletedFalse(ContentStatus contentStatus);

    // 조회수 증가
    @Modifying
    @Query("""
            UPDATE Content c SET c.viewCnt = c.viewCnt + 1 WHERE c.contentId = :contentId
            """)
    void increaseViewCnt(@Param("contentId") Long contentId);

    // 공개일이거나 지난 콘텐츠 - READY - PUBLISHED
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Content c SET c.contentStatus = :publishedStatus
            WHERE c.contentStatus = :readyStatus AND c.releaseAt <= :today AND (c.endAt IS NULL OR c.endAt > :today) AND c.deleted = false 
    """)
    int publishedContents(@Param("today") LocalDate today, @Param("readyStatus") ContentStatus readyStatus, @Param("publishedStatus") ContentStatus publishedStatus);

    // 종료일이 됐거나 지난 콘텐츠 READY OR PUBLISHED - END
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Content c SET c.contentStatus = :endStatus
            WHERE c.contentStatus in :targetStatuses AND c.endAt IS NOT NULL AND c.endAt <= :today AND c.deleted = false
    """)
    int endContents(@Param("today") LocalDate today, @Param("targetStatuses") Collection<ContentStatus> targetStatuses, @Param("endStatus") ContentStatus endStatus);
}
