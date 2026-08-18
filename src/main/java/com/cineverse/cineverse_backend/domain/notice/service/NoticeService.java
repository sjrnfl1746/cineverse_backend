package com.cineverse.cineverse_backend.domain.notice.service;

import com.cineverse.cineverse_backend.domain.notice.dto.request.NoticeRequestDTO;
import com.cineverse.cineverse_backend.domain.notice.dto.request.SearchNoticeDTO;
import com.cineverse.cineverse_backend.domain.notice.dto.response.NoticeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {

    // 공지 등록
    void addNotice(NoticeRequestDTO noticeRequestDTO);

    // 공지 수정
    void updateNotice(Long noticeId, NoticeRequestDTO noticeRequestDTO);

    // 공지 삭제
    void deleteNotice(Long noticeId);

    // 공지 조회
    NoticeResponseDTO getNoticeById(Long noticeId);

    // 공지 리스트 조회
    Page<NoticeResponseDTO> getAllNotices(Pageable pageable);

    // 공지 리스트 조회 - 등록 순서대로(관리자 용)
    Page<NoticeResponseDTO> getAlNoticesOrderByCreatedAtDesc(SearchNoticeDTO searchNoticeDTO, Pageable pageable);
}
