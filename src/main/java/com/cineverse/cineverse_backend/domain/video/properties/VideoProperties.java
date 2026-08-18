package com.cineverse.cineverse_backend.domain.video.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
public class VideoProperties {

    @Value("${file.upload.root-path}")
    private String rootPath;

    private Set<String> ALLOWED_MIME_TYPES = Set.of(
            "video/mp4", // mp4
            "video/webm", // webm
            "video/quicktime" // mov
    );

    @Value("${file.upload.video-max-size}")
    private Long MAX_SIZE; // 500MB
}
