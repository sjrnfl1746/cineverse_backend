package com.cineverse.cineverse_backend.domain.image.entity;

import com.cineverse.cineverse_backend.domain.image.enums.ImageTargetType;
import com.cineverse.cineverse_backend.domain.image.enums.ImageType;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Enumerated(EnumType.STRING)
    private ImageTargetType targetType;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    private String ogName;

    private String saveName;

    private String path;

    private Long size;

    private int sortOrder;

    private String mimeType;

    private boolean primaryImage;

    // 이미지 수정
    public void update(String ogName, String saveName, String path, Long size, int sortOrder, String mimeType, boolean primaryImage) {
        this.ogName = ogName;
        this.saveName = saveName;
        this.path = path;
        this.size = size;
        this.sortOrder = sortOrder;
        this.mimeType = mimeType;
        this.primaryImage = primaryImage;
    }
}
