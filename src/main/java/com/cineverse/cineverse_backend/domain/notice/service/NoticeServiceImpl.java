package com.cineverse.cineverse_backend.domain.notice.service;

import com.cineverse.cineverse_backend.domain.notice.dto.request.NoticeRequestDTO;
import com.cineverse.cineverse_backend.domain.notice.dto.request.SearchNoticeDTO;
import com.cineverse.cineverse_backend.domain.notice.dto.response.NoticeResponseDTO;
import com.cineverse.cineverse_backend.domain.notice.entity.Notice;
import com.cineverse.cineverse_backend.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    @Override
    public void addNotice(NoticeRequestDTO noticeRequestDTO) {

        Notice notice = Notice.builder()
                .title(noticeRequestDTO.getTitle())
                .content(noticeRequestDTO.getContent())
                .pinned(noticeRequestDTO.isPinned())
                .build();
        noticeRepository.save(notice);
    }

    @Transactional
    @Override
    public void updateNotice(Long noticeId, NoticeRequestDTO noticeRequestDTO) {

        // 공지 조회
        Notice notice = noticeRepository.findByNoticeIdAndDeletedFalse(noticeId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 공지입니다."));

        notice.update(noticeRequestDTO.getTitle(), noticeRequestDTO.getContent(), noticeRequestDTO.isPinned());
    }

    @Transactional
    @Override
    public void deleteNotice(Long noticeId) {

        // 공지 조회
        Notice notice = noticeRepository.findByNoticeIdAndDeletedFalse(noticeId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 공지입니다."));

        notice.delete();
    }

    @Transactional
    @Override
    public NoticeResponseDTO getNoticeById(Long noticeId) {

        // 공지 조회
        Notice notice = noticeRepository.findByNoticeIdAndDeletedFalse(noticeId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 공지입니다."));

        // 조회수 + 1
        notice.increaseViewCnt();

        return NoticeResponseDTO.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .viewCnt(notice.getViewCnt())
                .pinned(notice.isPinned())
                .createdAt(notice.getCreatedAt())
                .build();
    }

    @Override
    public Page<NoticeResponseDTO> getAllNotices(Pageable pageable) {
        return noticeRepository.findAllNotice(pageable);
    }

    @Override
    public Page<NoticeResponseDTO> getAlNoticesOrderByCreatedAtDesc(SearchNoticeDTO searchNoticeDTO, Pageable pageable) {
        return noticeRepository.findAllNoticeOrderByCreatedAtDesc(searchNoticeDTO, pageable);
    }
}
