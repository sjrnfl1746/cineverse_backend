package com.cineverse.cineverse_backend.domain.event.service;

import com.cineverse.cineverse_backend.domain.event.dto.request.EventModifyRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.request.EventRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.request.SearchEventRequestDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventListResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventOneResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventResponseDTO;
import com.cineverse.cineverse_backend.domain.event.dto.response.EventSummaryResponseDTO;
import com.cineverse.cineverse_backend.domain.event.entity.Event;
import com.cineverse.cineverse_backend.domain.event.enums.EventStatus;
import com.cineverse.cineverse_backend.domain.event.repository.EventRepository;
import com.cineverse.cineverse_backend.domain.image.enums.ImageTargetType;
import com.cineverse.cineverse_backend.domain.image.enums.ImageType;
import com.cineverse.cineverse_backend.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ImageService imageService;

    @Transactional
    @Override
    public void addEvent(EventRequestDTO eventRequestDTO, MultipartFile file) {

        // 이벤트 저장
        Event event = Event.builder()
                .title(eventRequestDTO.getTitle())
                .description(eventRequestDTO.getDescription())
                .startAt(eventRequestDTO.getStartAt())
                .endAt(eventRequestDTO.getEndAt())
                .build();
        eventRepository.save(event);

        // 이미지 저장
        imageService.saveImage(file, ImageTargetType.EVENT, event.getEventId(), ImageType.POSTER, 0, true);
    }

    @Override
    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAllEvents();
    }

    @Transactional
    @Override
    public List<EventSummaryResponseDTO> getEventSummary() {

        List<EventSummaryResponseDTO> eventSummaryResponseDTOList = new ArrayList<>();

        // 전체 이벤트 - 삭제된 이벤트 제외
        Long totalEvent = eventRepository.countByDeletedFalse();
        EventSummaryResponseDTO totalEventDTO = EventSummaryResponseDTO.builder()
                .title("전체 이벤트")
                .value(totalEvent)
                .build();
        eventSummaryResponseDTOList.add(totalEventDTO);

        // 진행중인 이벤트
        Long totalPublished = eventRepository.countByEventStatusAndDeletedFalse(EventStatus.PUBLISHED);
        EventSummaryResponseDTO publishedDTO = EventSummaryResponseDTO.builder()
                .title("진행 중")
                .value(totalPublished)
                .build();
        eventSummaryResponseDTOList.add(publishedDTO);

        // 진행예정 이벤트
        Long totalReady = eventRepository.countByEventStatusAndDeletedFalse(EventStatus.READY);
        EventSummaryResponseDTO readyDTO = EventSummaryResponseDTO.builder()
                .title("진행 예정")
                .value(totalReady)
                .build();
        eventSummaryResponseDTOList.add(readyDTO);

        // 결과 발표 대기 이벤트
        Long totalPending = eventRepository.countByEventStatusAndDeletedFalse(EventStatus.RESULT_PENDING);
        EventSummaryResponseDTO pendingDTO = EventSummaryResponseDTO.builder()
                .title("발표 대기")
                .value(totalPending)
                .build();
        eventSummaryResponseDTOList.add(pendingDTO);

        return eventSummaryResponseDTOList;
    }

    @Override
    public Page<EventListResponseDTO> getAllEvents(SearchEventRequestDTO searchEventRequestDTO, Pageable pageable) {
        return eventRepository.findByStatusAndKeyword(searchEventRequestDTO, pageable);
    }

    @Override
    public EventOneResponseDTO getEventById(Long eventId) {
        return eventRepository.findEventById(eventId);
    }

    @Transactional
    @Override
    public void updateEvent(EventModifyRequestDTO eventModifyRequestDTO, MultipartFile file, Long eventId) {

        // 이벤트 조회
        Event event = eventRepository.findByEventIdAndDeletedFalse(eventId).orElseThrow(
                () -> new RuntimeException("이벤트가 존재하지 않습니다."));

        // 이벤트 수정
        event.updateEvent(eventModifyRequestDTO.getTitle(), eventModifyRequestDTO.getDescription(),
                eventModifyRequestDTO.getStartAt(), eventModifyRequestDTO.getEndAt(), eventModifyRequestDTO.getEventStatus());

        // 포스터 재등록시
        if (file != null && !file.isEmpty()) {
            imageService.updateImage(file, ImageTargetType.EVENT, eventId, ImageType.POSTER, 0, true);
        }
    }

    @Transactional
    @Override
    public void deleteEvent(Long eventId) {

        // 이벤트 조회
        Event event = eventRepository.findByEventIdAndDeletedFalse(eventId).orElseThrow(
                () -> new RuntimeException("이벤트가 존재하지 않습니다."));
        event.deleteEvent();
    }

    @Override
    public EventOneResponseDTO getPublishedEventById(Long eventId) {
        return eventRepository.findPublishedEventById(eventId);
    }

    @Transactional
    @Override
    public int updateEventStatuses() {

        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));

        int publishedCnt = eventRepository.publishedEvents(now, EventStatus.READY, EventStatus.PUBLISHED);
        int resultPendingCnt = eventRepository.resultPendingEvents(now, EventStatus.PUBLISHED, EventStatus.RESULT_PENDING);

        return publishedCnt + resultPendingCnt;
    }
}
