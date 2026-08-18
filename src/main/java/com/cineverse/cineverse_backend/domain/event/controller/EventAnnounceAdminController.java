package com.cineverse.cineverse_backend.domain.event.controller;

import com.cineverse.cineverse_backend.domain.event.dto.request.EventAnnounceRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventAnnouncementResponseDTO;
import com.cineverse.cineverse_backend.domain.event.service.EventAnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/eventAnnounce")
@Tag(name = "Admin Event Announce", description = "관리자 이벤트 결과 발표 관리 API")
public class EventAnnounceAdminController {

    private final EventAnnouncementService eventAnnouncementService;

    @Operation(summary = "이벤트 결과 발표 등록", description = "이벤트 결과 발표 등록")
    @PostMapping("/{eventId}")
    public ResponseEntity<Void> addEventAnnouncement(@PathVariable Long eventId,
                                                     @RequestBody EventAnnounceRequestDTO eventAnnounceRequestDTO) {
        eventAnnouncementService.addEventAnnouncement(eventId, eventAnnounceRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "이벤트 결과 발표 수정", description = "이벤트 결과 발표 수정")
    @PutMapping("/{eventId}")
    public ResponseEntity<Void> updateEventAnnouncement(@PathVariable Long eventId,
                                                        @RequestBody EventAnnounceRequestDTO eventAnnounceRequestDTO) {
        eventAnnouncementService.modifyEventAnnouncement(eventId, eventAnnounceRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "이벤트 결과 발표 삭제", description = "이벤트 결과 발표 삭제")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEventAnnouncement(@PathVariable Long eventId) {
        eventAnnouncementService.deleteEventAnnouncement(eventId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "이벤트 결과 발표 단건 조회", description = "이벤트 결과 발표 단건 조회")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventAnnouncementResponseDTO> getEventAnnouncement(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventAnnouncementService.getEventAnnouncementByEventId(eventId));
    }
}
