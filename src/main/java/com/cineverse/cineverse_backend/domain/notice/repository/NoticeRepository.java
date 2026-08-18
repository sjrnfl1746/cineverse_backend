package com.cineverse.cineverse_backend.domain.notice.repository;

import com.cineverse.cineverse_backend.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {

    // 삭제되지 않는 공지 조회
    Optional<Notice> findByNoticeIdAndDeletedFalse(Long noticeId);
}
