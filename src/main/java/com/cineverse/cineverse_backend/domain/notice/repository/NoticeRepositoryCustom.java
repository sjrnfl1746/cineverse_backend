package com.cineverse.cineverse_backend.domain.notice.repository;

import com.cineverse.cineverse_backend.domain.notice.dto.request.SearchNoticeDTO;
import com.cineverse.cineverse_backend.domain.notice.dto.response.NoticeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    // 공지 조회
    Page<NoticeResponseDTO> findAllNotice(Pageable pageable);

    // 공지 조회 - 관리자용 - 등록 순서대로
    Page<NoticeResponseDTO> findAllNoticeOrderByCreatedAtDesc(SearchNoticeDTO searchNoticeDTO, Pageable pageable);
}
