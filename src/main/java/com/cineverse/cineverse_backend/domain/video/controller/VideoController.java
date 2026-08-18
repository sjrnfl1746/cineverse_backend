package com.cineverse.cineverse_backend.domain.video.controller;

import com.cineverse.cineverse_backend.domain.video.dto.response.VideoResponseDTO;
import com.cineverse.cineverse_backend.domain.video.service.VideoService;
import com.cineverse.cineverse_backend.global.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/video")
@Tag(name = "Video", description = "영상 관리 API")
public class VideoController {

    private final VideoService videoService;

    @Operation(summary = "영상 조회", description = "contentId로 영상 url 조회")
    @GetMapping("/{contentId}")
    public ResponseEntity<VideoResponseDTO> getVideoByContentId(@PathVariable Long contentId,
                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(videoService.getVideoByUserIdAndContentId(userDetails.getUserId(), contentId));
    }
}
