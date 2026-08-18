package com.cineverse.cineverse_backend.domain.video.entity;

import com.cineverse.cineverse_backend.domain.content.entity.Content;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "video")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Video extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    private String ogName; // 원본 이름

    private String saveName; // 저장할 이름

    private String path;

    private Long size;

    private String mimeType;

    @Builder.Default
    private boolean active = true; // 영상 사용 여부

    @Builder.Default
    private boolean deleted = false; // 영상 삭제 여부

    // 영상 수정
    public void update(String ogName, String saveName, String path,
                       Long size, String mimeType) {
        this.ogName = ogName;
        this.saveName = saveName;
        this.path = path;
        this.size = size;
        this.mimeType = mimeType;
    }
}
