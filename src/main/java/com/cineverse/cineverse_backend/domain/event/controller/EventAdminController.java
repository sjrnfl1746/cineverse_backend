package com.cineverse.cineverse_backend.domain.event.controller;

import com.cineverse.cineverse_backend.domain.event.dto.request.EventModifyRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.request.EventRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.request.SearchEventRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventListResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventOneResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventSummaryResponseDTO;
import com.cineverse.cineverse_backend.domain.event.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/event")
@Tag(name = "Admin Event", description = "관리자 이벤트 관리 API")
public class EventAdminController {

    private final EventService eventService;

    @Operation(summary = "이벤트 요약 조회", description = "이벤트 관련 정보 요약 조회")
    @GetMapping("/summary")
    public ResponseEntity<List<EventSummaryResponseDTO>> summary() {
        return ResponseEntity.ok(eventService.getEventSummary());
    }

    @Operation(summary = "이벤트 등록", description = "이벤트 등록")
    @PostMapping
    public ResponseEntity<Void> addEvent(
            @Valid @RequestPart("event") EventRequestDTO eventRequestDTO,
            @Valid @RequestPart("poster") MultipartFile multipartFile) {
        eventService.addEvent(eventRequestDTO, multipartFile);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "이벤트 리스트 조회", description = "이벤트 리스트 조회")
    @GetMapping
    public ResponseEntity<Page<EventListResponseDTO>> getEventList(
            SearchEventRequestDTO searchEventRequestDTO,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(eventService.getAllEvents(searchEventRequestDTO, pageable));
    }

    @Operation(summary = "이벤트 단건 조회", description = "eventId로 이벤트 단건 조회")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventOneResponseDTO> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @Operation(summary = "이벤트 수정", description = "이벤트 수정")
    @PutMapping("/{eventId}")
    public ResponseEntity<Void> modifyEvent(
            @PathVariable Long eventId,
            @Valid @RequestPart("event") EventModifyRequestDTO eventModifyRequestDTO,
            @Valid @RequestPart(value = "poster", required = false) MultipartFile file) {
        eventService.updateEvent(eventModifyRequestDTO, file, eventId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "이벤트 삭제", description = "이벤트 삭제")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> removeEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
