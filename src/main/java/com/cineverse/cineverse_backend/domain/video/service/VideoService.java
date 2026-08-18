package com.cineverse.cineverse_backend.domain.video.service;

import com.cineverse.cineverse_backend.domain.video.dto.response.VideoResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {

    // 콘텐츠 관련 영상 저장
    void addVideo(Long contentId, MultipartFile video);

    // contentId로 영상 존재 여부 확인
    Boolean existsVideo(Long contentId);

    // contentId로 영상 조회
    VideoResponseDTO getVideoByContentId(Long contentId);

    // 영상 수정
    void modifyVideo(Long contentId, MultipartFile video);

    // contentId로 영상 조회
    VideoResponseDTO getVideoByUserIdAndContentId(Long userId, Long contentId);
}
