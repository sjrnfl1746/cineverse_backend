package com.cineverse.cineverse_backend.domain.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaverNewsItemDTO {

    private String title;

    private String originallink;

    private String link;

    private String description;

    private String pubDate;
}
