package com.cineverse.cineverse_backend.domain.event.repository;

import com.cineverse.cineverse_backend.domain.event.dto.response.EventAnnounceWinnerResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventAnnouncementRepositoryCustom {

    Page<EventAnnounceWinnerResponseDTO> findAllEventAnnouncementList(Pageable pageable);
}
