package com.cineverse.cineverse_backend.domain.event.service;

import com.cineverse.cineverse_backend.domain.event.dto.request.EventAnnounceRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventAnnounceWinnerResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventAnnouncementResponseDTO;
import com.cineverse.cineverse_backend.domain.event.entity.Event;
import com.cineverse.cineverse_backend.domain.event.entity.EventAnnouncement;
import com.cineverse.cineverse_backend.domain.event.enums.EventStatus;
import com.cineverse.cineverse_backend.domain.event.repository.EventAnnouncementRepository;
import com.cineverse.cineverse_backend.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventAnnouncementServiceImpl implements EventAnnouncementService {

    private final EventAnnouncementRepository eventAnnouncementRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public void addEventAnnouncement(Long eventId, EventAnnounceRequestDTO eventAnnounceRequestDTO) {

        // 이벤트 조회
        Event event = eventRepository.findByEventIdAndDeletedFalse(eventId).orElseThrow(
                () -> new RuntimeException("이벤트가 존재하지 않습니다."));

        // 이벤트 저장
        EventAnnouncement eventAnnouncement = EventAnnouncement.builder()
                .title(eventAnnounceRequestDTO.getTitle())
                .description(eventAnnounceRequestDTO.getDescription())
                .event(event)
                .build();
        eventAnnouncementRepository.save(eventAnnouncement);

        // 이벤트 상태도 수정
        event.modifyEventStatus(EventStatus.END);
    }

    @Transactional
    @Override
    public void modifyEventAnnouncement(Long eventId, EventAnnounceRequestDTO eventAnnounceRequestDTO) {

        // eventId로 이벤트 결과 발표 조회
        EventAnnouncement eventAnnouncement = eventAnnouncementRepository.findByEvent_EventIdAndDeletedFalse(eventId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 이벤트 결과 입니다."));

        // 수정
        eventAnnouncement.updateEventAnnouncement(eventAnnounceRequestDTO.getTitle(), eventAnnounceRequestDTO.getDescription());
    }

    @Transactional
    @Override
    public void deleteEventAnnouncement(Long eventId) {

        // 이벤트 조회
        Event event = eventRepository.findByEventIdAndDeletedFalse(eventId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 이벤트 입니다."));

        // eventId로 이벤트 결과 발표 조회
        EventAnnouncement eventAnnouncement = eventAnnouncementRepository.findByEvent_EventIdAndDeletedFalse(eventId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 이벤트 결과 입니다."));

        // 삭제
        eventAnnouncement.deleteEventAnnouncement();

        // 이벤트 상태도 변경
        event.modifyEventStatus(EventStatus.RESULT_PENDING);
    }

    @Transactional
    @Override
    public EventAnnouncementResponseDTO getEventAnnouncementByEventId(Long eventId) {

        // eventId로 이벤트 결과 발표 조회
        EventAnnouncement eventAnnouncement = eventAnnouncementRepository.findByEvent_EventIdAndDeletedFalse(eventId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 이벤트 결과 입니다."));

        return EventAnnouncementResponseDTO.builder()
                .eventAnnouncementId(eventAnnouncement.getEventAnnouncementId())
                .title(eventAnnouncement.getTitle())
                .description(eventAnnouncement.getDescription())
                .createdAt(eventAnnouncement.getCreatedAt())
                .build();
    }

    @Override
    public Page<EventAnnounceWinnerResponseDTO> getAllEventAnnouncement(Pageable pageable) {
        return eventAnnouncementRepository.findAllEventAnnouncementList(pageable);
    }
}
