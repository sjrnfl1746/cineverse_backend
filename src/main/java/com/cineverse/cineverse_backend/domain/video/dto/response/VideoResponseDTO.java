package com.cineverse.cineverse_backend.domain.video.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponseDTO {

    private Long videoId;

    private String ogName;

    private String saveName;

    private String path;

    private Long size;

    private String mimeType;

    private boolean active;

    private boolean deleted;
}
