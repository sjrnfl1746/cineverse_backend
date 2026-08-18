package com.cineverse.cineverse_backend.domain.notice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponseDTO {

    private Long noticeId;

    private String title;

    private String content;

    private Long viewCnt;

    private boolean pinned;

    private LocalDateTime createdAt;
}
