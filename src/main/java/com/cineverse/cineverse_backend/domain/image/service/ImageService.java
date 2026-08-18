package com.cineverse.cineverse_backend.domain.image.service;

import com.cineverse.cineverse_backend.domain.image.enums.ImageTargetType;
import com.cineverse.cineverse_backend.domain.image.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    // 이미지 파일 저장
    void saveImage(MultipartFile file, ImageTargetType targetType, Long targetId, ImageType imageType, int sortOrder, boolean primaryImage);

    // 이미지 파일 수정
    void updateImage(MultipartFile file, ImageTargetType targetType, Long targetId, ImageType imageType, int sortOrder, boolean primaryImage);
}
