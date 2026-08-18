package com.cineverse.cineverse_backend.domain.image.repository;

import com.cineverse.cineverse_backend.domain.image.entity.Image;
import com.cineverse.cineverse_backend.domain.image.enums.ImageTargetType;
import com.cineverse.cineverse_backend.domain.image.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    // targetType, targetId로 이미지 조회
    Optional<Image> findByTargetTypeAndTargetIdAndImageType(ImageTargetType targetType, Long targetId, ImageType imageType);
}
