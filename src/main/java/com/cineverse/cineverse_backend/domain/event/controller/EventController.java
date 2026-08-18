package com.cineverse.cineverse_backend.domain.event.controller;

import com.cineverse.cineverse_backend.domain.event.dto.response.EventOneResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventResponseDTO;
import com.cineverse.cineverse_backend.domain.event.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
@Tag(name = "Event", description = "이벤트 API")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "이벤트 리스트 조회", description = "이벤트 리스트 조회")
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @Operation(summary = "이벤트 단건 조회", description = "eventId로 이벤트 단건 조회")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventOneResponseDTO> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getPublishedEventById(eventId));
    }
}
