package com.cineverse.cineverse_backend.domain.notice.controller;

import com.cineverse.cineverse_backend.domain.notice.dto.request.NoticeRequestDTO;
import com.cineverse.cineverse_backend.domain.notice.dto.request.SearchNoticeDTO;
import com.cineverse.cineverse_backend.domain.notice.dto.response.NoticeResponseDTO;
import com.cineverse.cineverse_backend.domain.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notice")
@Tag(name = "Admin Notice", description = "관리자 공지사항 관리 API")
public class AdminNoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 등록", description = "공지사항 등록")
    @PostMapping
    public ResponseEntity<Void> addNotice(
            @RequestBody NoticeRequestDTO noticeRequestDTO) {
        noticeService.addNotice(noticeRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공지사항 수정", description = "공지사항 수정")
    @PutMapping("/{noticeId}")
    public ResponseEntity<Void> modifyNotice(@PathVariable Long noticeId,
                                             @RequestBody NoticeRequestDTO noticeRequestDTO) {
        noticeService.updateNotice(noticeId, noticeRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공지사항 삭제", description = "공지사항 삭제")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공지사항 단건 조회", description = "공지사항 단건 조회")
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDTO> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getNoticeById(noticeId));
    }

    @Operation(summary = "공지사항 리스트 조회", description = "공지사항 리스트 조회")
    @GetMapping
    public ResponseEntity<Page<NoticeResponseDTO>> getAllNotice(
            SearchNoticeDTO searchNoticeDTO,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(noticeService.getAlNoticesOrderByCreatedAtDesc(searchNoticeDTO, pageable));
    }
}
