package com.cineverse.cineverse_backend.domain.video.repository;

import com.cineverse.cineverse_backend.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {

    // contentId로 영상 존재 여부 확인
    Boolean existsByContent_ContentId(Long contentId);

    // contentId로 영상 조회
    Optional<Video> findByContent_ContentId(Long contentId);
}
