package com.cineverse.cineverse_backend.domain.event.repository;

import com.cineverse.cineverse_backend.domain.event.dto.request.SearchEventRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventListResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventOneResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventRepositoryCustom {

    // 진행중, 삭제 x 이벤트 목록 조회
    List<EventResponseDTO> findAllEvents();

    Page<EventListResponseDTO> findByStatusAndKeyword(SearchEventRequestDTO searchEventRequestDTO, Pageable pageable);

    // 이벤트 조회
    EventOneResponseDTO findEventById(Long eventId);

    // 이벤트 조회 - published인 것 조회
    EventOneResponseDTO findPublishedEventById(Long eventId);
}
