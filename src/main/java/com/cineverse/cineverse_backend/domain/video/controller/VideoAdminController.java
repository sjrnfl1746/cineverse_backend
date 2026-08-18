package com.cineverse.cineverse_backend.domain.video.controller;

import com.cineverse.cineverse_backend.domain.video.dto.response.VideoResponseDTO;
import com.cineverse.cineverse_backend.domain.video.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/video")
@Tag(name = "Admin Video", description = "관리자 영상 관리 API")
public class VideoAdminController {

    private final VideoService videoService;

    @Operation(summary = "영상 등록", description = "콘텐츠 관련 영상 등록")
    @PostMapping("/{contentId}")
    public ResponseEntity<Void> addVideo(@PathVariable Long contentId,
                                         @RequestPart("video") MultipartFile video) {
        videoService.addVideo(contentId, video);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "영상 존재여부 조회", description = "contentId로 영상 존재여부 확인")
    @GetMapping("/{contentId}/exists")
    public ResponseEntity<Boolean> existVideo(@PathVariable Long contentId) {
        return ResponseEntity.ok(videoService.existsVideo(contentId));
    }

    @Operation(summary = "영상 단건 조회", description = "contentId로 영상 단건 조회")
    @GetMapping("/{contentId}")
    public ResponseEntity<VideoResponseDTO> getVideo(@PathVariable Long contentId) {
        return ResponseEntity.ok(videoService.getVideoByContentId(contentId));
    }

    @Operation(summary = "영상 수정", description = "영상 수정")
    @PutMapping("/{contentId}")
    public ResponseEntity<Void> modifyVideo(@PathVariable Long contentId,
                                            @RequestPart("video") MultipartFile video) {
        videoService.modifyVideo(contentId, video);
        return ResponseEntity.noContent().build();
    }
}
