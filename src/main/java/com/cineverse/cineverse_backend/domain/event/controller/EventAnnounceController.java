package com.cineverse.cineverse_backend.domain.event.controller;

import com.cineverse.cineverse_backend.domain.event.dto.response.EventAnnounceWinnerResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventAnnouncementResponseDTO;
import com.cineverse.cineverse_backend.domain.event.service.EventAnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/eventAnnounce")
@Tag(name = "Event Announce", description = "이벤트 결과 발표 관리 API")
public class EventAnnounceController {

    private final EventAnnouncementService eventAnnouncementService;

    @Operation(summary = "이벤트 결과 발표 단건 조회", description = "이벤트 결과 발표 단건 조회")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventAnnouncementResponseDTO> getEventAnnouncement(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventAnnouncementService.getEventAnnouncementByEventId(eventId));
    }

    @Operation(summary = "이벤트 결과 발표 리스트 조회", description = "이벤트 결과 발표 리스트 조회")
    @GetMapping
    public ResponseEntity<Page<EventAnnounceWinnerResponseDTO>> getEventAnnouncementList(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(eventAnnouncementService.getAllEventAnnouncement(pageable));
    }
}
