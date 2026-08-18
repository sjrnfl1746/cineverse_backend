package com.cineverse.cineverse_backend.domain.notice.entity;

import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    private Long viewCnt = 0L;

    @Builder.Default
    private boolean pinned = false;

    @Builder.Default
    private boolean deleted = false;

    // 수정
    public void update(String title, String content, boolean pinned) {
        this.title = title;
        this.content = content;
        this.pinned = pinned;
    }

    // 조회 수 증가
    public void increaseViewCnt() {
        this.viewCnt++;
    }

    // 삭제
    public void delete() {
        this.deleted = true;
    }
}
