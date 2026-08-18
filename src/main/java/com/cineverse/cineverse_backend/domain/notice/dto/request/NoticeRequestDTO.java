package com.cineverse.cineverse_backend.domain.notice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRequestDTO {

    private String title;

    private String content;

    private boolean pinned;
}
