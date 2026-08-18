package com.cineverse.cineverse_backend.domain.notice.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
@Tag(name = "Notice", description = "공지사항 API")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 단건 조회", description = "공지사항 단건 조회")
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDTO> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getNoticeById(noticeId));
    }

    @Operation(summary = "공지사항 리스트 조회", description = "공지사항 리스트 조회")
    @GetMapping
    public ResponseEntity<Page<NoticeResponseDTO>> getAllNotice(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(noticeService.getAllNotices(pageable));
    }
}
