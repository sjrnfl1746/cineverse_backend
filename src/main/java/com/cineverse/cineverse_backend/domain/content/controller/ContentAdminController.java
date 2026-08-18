package com.cineverse.cineverse_backend.domain.content.controller;

import com.cineverse.cineverse_backend.domain.content.dto.request.ContentRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.SearchRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentDetailResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentResponseDTO;
import com.cineverse.cineverse_backend.domain.content.service.content.ContentService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/content")
@Tag(name = "Admin Content", description = "관리자 콘텐츠 관리 API")
public class ContentAdminController {

    private final ContentService contentService;

    @Operation(summary = "콘텐츠 등록", description = "콘텐츠 등록")
    @PostMapping
    public ResponseEntity<Long> addContent(
            @Valid @RequestPart("content") ContentRequestDTO contentRequestDTO,
            @Valid @RequestPart("poster") MultipartFile multipartFile) {

        Long contentId = contentService.addContent(contentRequestDTO, multipartFile);

        return ResponseEntity.ok(contentId);
    }

    @Operation(summary = "콘텐츠 리스트 조회", description = "콘텐츠 리스트 조회")
    @GetMapping
    public ResponseEntity<Page<ContentResponseDTO>> getContentList(
            SearchRequestDTO searchRequestDTO,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(contentService.getAllContent(searchRequestDTO, pageable));
    }

    @Operation(summary = "콘텐츠 단건 조회", description = "contentId로 콘텐츠 단건 조회")
    @GetMapping("/{contentId}")
    public ResponseEntity<ContentDetailResponseDTO> getContentByContentId(@PathVariable Long contentId) {

        return ResponseEntity.ok(contentService.getContentByContentId(contentId));
    }

    @Operation(summary = "콘텐츠 수정", description = "콘텐츠 정보 수정")
    @PutMapping("/{contentId}")
    public ResponseEntity<Void> modifyContent(
            @PathVariable Long contentId,
            @Valid @RequestPart("content") ContentRequestDTO contentRequestDTO,
            @RequestPart(value = "poster", required = false) MultipartFile poster) {
        contentService.updateContent(contentRequestDTO, poster, contentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "콘텐츠 삭제", description = "콘텐츠 삭제")
    @DeleteMapping("/{contentId}")
    public ResponseEntity<Void> removeContent(@PathVariable Long contentId) {
        contentService.deleteContent(contentId);
        return ResponseEntity.noContent().build();
    }
}

