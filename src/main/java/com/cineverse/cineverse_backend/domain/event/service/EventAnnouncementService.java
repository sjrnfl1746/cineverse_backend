package com.cineverse.cineverse_backend.domain.event.service;

import com.cineverse.cineverse_backend.domain.event.dto.request.EventAnnounceRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventAnnounceWinnerResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventAnnouncementResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventAnnouncementService {

    // 이벤트 결과 발표 추가
    void addEventAnnouncement(Long eventId, EventAnnounceRequestDTO eventAnnounceRequestDTO);

    // 이벤트 결과 발표 수정
    void modifyEventAnnouncement(Long eventId, EventAnnounceRequestDTO eventAnnounceRequestDTO);

    // 이벤트 결과 발표 삭제
    void deleteEventAnnouncement(Long eventId);

    // 이벤트 결과 발표 단건 조회
    EventAnnouncementResponseDTO getEventAnnouncementByEventId(Long eventId);

    // 이벤트 결과 리스트 조회
    Page<EventAnnounceWinnerResponseDTO> getAllEventAnnouncement(Pageable pageable);
}
