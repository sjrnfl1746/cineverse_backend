package com.cineverse.cineverse_backend.domain.event.service;

import com.cineverse.cineverse_backend.domain.event.dto.request.EventModifyRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.request.EventRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.request.SearchEventRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventListResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventOneResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventSummaryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {

    // 이벤트 등록
    void addEvent(EventRequestDTO eventRequestDTO, MultipartFile file);

    // 이벤트 리스트 조회 - 사용자 부분, deleted = false, eventStatus = PUBLISHED
    List<EventResponseDTO> getAllEvents();

    // 이벤트 요약 조회
    List<EventSummaryResponseDTO> getEventSummary();

    // 이벤트 리스트 조회
    Page<EventListResponseDTO> getAllEvents(SearchEventRequestDTO searchEventRequestDTO, Pageable pageable);

    // 이벤트 단건 조회
    EventOneResponseDTO getEventById(Long eventId);

    // 이벤트 수정
    void updateEvent(EventModifyRequestDTO eventModifyRequestDTO, MultipartFile file, Long eventId);

    // 이벤트 삭제
    void deleteEvent(Long eventId);

    // 이벤트 단건 조회 - published된 이벤트
    EventOneResponseDTO getPublishedEventById(Long eventId);

    // 이벤트 상태 변경
    int updateEventStatuses();
}
