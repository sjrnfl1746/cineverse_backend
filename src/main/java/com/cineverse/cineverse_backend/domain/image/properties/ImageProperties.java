package com.cineverse.cineverse_backend.domain.image.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
public class ImageProperties {

    @Value("${file.upload.root-path}")
    private String rootPath;

    private Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    @Value("${file.upload.image-max-size}")
    private Long MAX_SIZE; // 10MB
}
