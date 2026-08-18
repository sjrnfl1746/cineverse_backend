package com.cineverse.cineverse_backend.domain.image.dto;

import com.cineverse.cineverse_backend.domain.image.enums.ImageTargetType;
import com.cineverse.cineverse_backend.domain.image.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponseDTO {

    private Long imageId;

    private ImageTargetType targetType;
    private Long targetId;

    private ImageType imageType;

    private String ogName;

    private String saveName;

    private String path;

    private int sortOrder;

    private String mimeType;

    private boolean primaryImage;
}
