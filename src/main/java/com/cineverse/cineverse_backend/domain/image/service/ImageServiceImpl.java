package com.cineverse.cineverse_backend.domain.image.service;

import com.cineverse.cineverse_backend.domain.image.entity.Image;
import com.cineverse.cineverse_backend.domain.image.enums.ImageTargetType;
import com.cineverse.cineverse_backend.domain.image.enums.ImageType;
import com.cineverse.cineverse_backend.domain.image.properties.ImageProperties;
import com.cineverse.cineverse_backend.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageProperties imageProperties;

    @Transactional
    @Override
    public void saveImage(MultipartFile file, ImageTargetType targetType, Long targetId, ImageType imageType, int sortOrder, boolean primaryImage) {

        // 파일 유효성 검사
        validateImage(file);

        String ogName = file.getOriginalFilename();
        String extension = getExtension(ogName);
        String saveName = UUID.randomUUID() + extension;

        Path savePath = generatePath(targetType, imageType, saveName);

        try {
            // 부모 디렉터리 생성
            Files.createDirectories(savePath.getParent());

            // 실제 파일 저장
            file.transferTo(savePath);

            Image image = Image.builder()
                    .targetType(targetType)
                    .targetId(targetId)
                    .imageType(imageType)
                    .ogName(ogName)
                    .saveName(saveName)
                    .path(generateRelativePath(
                            targetType,
                            imageType,
                            saveName
                    ))
                    .size(file.getSize())
                    .sortOrder(sortOrder)
                    .mimeType(file.getContentType())
                    .primaryImage(primaryImage)
                    .build();

            imageRepository.save(image);
        } catch (IOException e) {
            deleteImage(savePath);
            throw new RuntimeException("파일 업로드 실패", e);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateImage(MultipartFile file, ImageTargetType targetType, Long targetId, ImageType imageType, int sortOrder, boolean primaryImage) {

        // 이미지 유효성 검사
        validateImage(file);

        // 기존 이미지 조회
        Image existImage = imageRepository.findByTargetTypeAndTargetIdAndImageType(targetType, targetId, imageType)
                .orElseThrow(() -> new RuntimeException("기존 이미지가 존재하지 않습니다."));

        // 기존 이미지 파일 경로
        Path oldPath = Path.of(
                imageProperties.getRootPath(),
                existImage.getPath()
        );

        String ogName = file.getOriginalFilename();
        String extension = getExtension(ogName);
        String saveName = UUID.randomUUID() + extension;

        // 새로 저장할 파일 경로
        Path newPath = generatePath(targetType, imageType, saveName);
        String relativePath = generateRelativePath(targetType, imageType, saveName);

        // 새로운 이미지 파일 저장 후 기존 이미지 파일 삭제
        try {
            Files.createDirectories(newPath.getParent());

            // 새 이미지 파일 저장
            file.transferTo(newPath);

            // 기존 엔티티의 이미지 파일 정보 수정
            existImage.update(ogName, saveName, relativePath, file.getSize(), sortOrder, file.getContentType(), primaryImage);

            // 기존 파일 삭제
            deleteImage(oldPath);
        } catch (IOException e) {
            deleteImage(newPath);
            throw new RuntimeException("파일 수정 실패", e);
        } catch (RuntimeException e) {
            deleteImage(newPath);
            throw e;
        }
    }

    // 파일 유효성 검사
    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        String mimeType = file.getContentType();
        if (mimeType == null || !imageProperties.getALLOWED_MIME_TYPES().contains(mimeType)) {
            throw new IllegalArgumentException("JPG, PNG, WEBP 형식만 업로드 가능합니다.");
        }

        if (file.getSize() > imageProperties.getMAX_SIZE()) {
            throw new IllegalArgumentException("파일 크기는 10MB 이하만 가능합니다.");
        }
    }

    // 파일 확장자 생성
    private String getExtension(String ogName) {
        return ogName.substring(ogName.lastIndexOf(".")).toLowerCase();
    }

    // 파일 경로 생성
    private Path generatePath(ImageTargetType targetType, ImageType imageType, String saveName) {
        LocalDate now = LocalDate.now();

        return Path.of(
                imageProperties.getRootPath(),
                "images",
                targetType.name().toLowerCase(),
                imageType.name().toLowerCase(),
                String.valueOf(now.getYear()),
                String.format("%02d", now.getMonthValue()),
                saveName
        );
    }

    // 파일 상대 경로 생성
    private String generateRelativePath(ImageTargetType targetType, ImageType imageType, String saveName) {
        LocalDate now = LocalDate.now();

        return Path.of(
                "images",
                targetType.name().toLowerCase(),
                imageType.name().toLowerCase(),
                String.valueOf(now.getYear()),
                String.format("%02d", now.getMonthValue()),
                saveName
        ).toString();
    }

    // 파일 삭제
    private void deleteImage(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }
}
