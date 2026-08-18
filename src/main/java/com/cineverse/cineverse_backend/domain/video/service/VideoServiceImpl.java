package com.cineverse.cineverse_backend.domain.video.service;

import com.cineverse.cineverse_backend.domain.content.entity.Content;
import com.cineverse.cineverse_backend.domain.content.repository.content.ContentRepository;
import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.domain.user.enums.UserStatus;
import com.cineverse.cineverse_backend.domain.user.repository.UserRepository;
import com.cineverse.cineverse_backend.domain.video.dto.response.VideoResponseDTO;
import com.cineverse.cineverse_backend.domain.video.entity.Video;
import com.cineverse.cineverse_backend.domain.video.properties.VideoProperties;
import com.cineverse.cineverse_backend.domain.video.repository.VideoRepository;
import com.cineverse.cineverse_backend.global.exception.BusinessException;
import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final ContentRepository contentRepository;
    private final VideoProperties videoProperties;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void addVideo(Long contentId, MultipartFile video) {

        // 콘텐츠 조회
        Content content = contentRepository.findById(contentId).filter(c -> !c.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠 입니다."));

        // 이미 영상이 등록되어 있는 콘텐츠 인지 확인
        if (videoRepository.existsByContent_ContentId(contentId)) {
            throw new IllegalArgumentException("이미 등록된 영상이 존재합니다.");
        }

        // 파일 유효성 검사
        validateVideo(video);

        String ogName = video.getOriginalFilename();
        String extension = getExtension(ogName);
        String saveName = UUID.randomUUID() + extension;

        Path savePath = generatePath(saveName);

        try {
            // 부모 디렉터리 생성
            Files.createDirectories(savePath.getParent());

            // 실제 파일 저장
            video.transferTo(savePath);

            Video videos = Video.builder()
                    .content(content)
                    .ogName(ogName)
                    .saveName(saveName)
                    .path(generateRelativePath(saveName))
                    .size(video.getSize())
                    .mimeType(video.getContentType())
                    .build();

            videoRepository.save(videos);
        } catch (IOException e) {
            deleteVideo(savePath);
            throw new RuntimeException("파일 업로드 실패", e);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    public Boolean existsVideo(Long contentId) {
        return videoRepository.existsByContent_ContentId(contentId);
    }

    @Transactional
    @Override
    public VideoResponseDTO getVideoByContentId(Long contentId) {
        Optional<Video> video = videoRepository.findByContent_ContentId(contentId);

        VideoResponseDTO videoResponseDTO = VideoResponseDTO.builder()
                .videoId(video.get().getVideoId())
                .ogName(video.get().getOgName())
                .saveName(video.get().getSaveName())
                .path(video.get().getPath())
                .size(video.get().getSize())
                .mimeType(video.get().getMimeType())
                .active(video.get().isActive())
                .deleted(video.get().isDeleted())
                .build();
        return videoResponseDTO;
    }

    @Transactional
    @Override
    public void modifyVideo(Long contentId, MultipartFile video) {

        // 기존 영상 존재 확인
        Optional<Video> existingVideo = videoRepository.findByContent_ContentId(contentId);

        // 기존 파일 경로
        Path oldPath = Path.of(videoProperties.getRootPath(), existingVideo.get().getPath());

        // 파일 유효성 검사
        validateVideo(video);

        String ogName = video.getOriginalFilename();
        String extension = getExtension(ogName);
        String saveName = UUID.randomUUID() + extension;

        Path savePath = generatePath(saveName);

        try {
            // 부모 디렉터리 생성
            Files.createDirectories(savePath.getParent());

            // 실제 파일 저장
            video.transferTo(savePath);

            // 기존 엔티티 정보 수정
            existingVideo.get().update(ogName, saveName, generateRelativePath(saveName), video.getSize(), video.getContentType());

            // 기존 실제 파일 삭제
            deleteVideo(oldPath);
        } catch (IOException e) {
            deleteVideo(savePath);
            throw new RuntimeException("파일 업로드 실패", e);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Transactional
    @Override
    public VideoResponseDTO getVideoByUserIdAndContentId(Long userId, Long contentId) {

        // 사용자 조회
        User user = userRepository.findByUserIdAndStatus(userId, UserStatus.ACTIVE).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 구독 여부 확인
        if (!user.isSubscribed()) {
            throw new RuntimeException("구독중이 아닙니다.");
        }

        // 영상 조회
        Video video = videoRepository.findByContent_ContentId(contentId).orElseThrow(
                () -> new RuntimeException("영상이 존재하지 않습니다."));

        return VideoResponseDTO.builder()
                .path(video.getPath())
                .build();
    }

    // 파일 유효성 검사
    private void validateVideo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        String mimeType = file.getContentType();
        if (mimeType == null || !videoProperties.getALLOWED_MIME_TYPES().contains(mimeType)) {
            throw new IllegalArgumentException("mp4, webm, mov 형식만 업로드 가능합니다.");
        }

        if (file.getSize() > videoProperties.getMAX_SIZE()) {
            throw new IllegalArgumentException("파일 크기는 500MB 이하만 가능합니다.");
        }
    }

    // 파일 확장자 생성
    private String getExtension(String ogName) {
        return ogName.substring(ogName.lastIndexOf(".")).toLowerCase();
    }

    // 파일 경로 생성
    private Path generatePath(String saveName) {
        LocalDate now = LocalDate.now();

        return Path.of(
                videoProperties.getRootPath(),
                "videos",
                String.valueOf(now.getYear()),
                String.format("%02d", now.getMonthValue()),
                saveName
        );
    }

    // 파일 상대 경로 생성
    private String generateRelativePath(String saveName) {
        LocalDate now = LocalDate.now();

        return Path.of(
                "videos",
                String.valueOf(now.getYear()),
                String.format("%02d", now.getMonthValue()),
                saveName
        ).toString();
    }

    // 파일 삭제
    private void deleteVideo(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }
}
