package com.cineverse.cineverse_backend.domain.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaverNewsResponseDTO {

    private String lastBuildDate;

    private Integer total;

    private Integer start;

    private Integer display;

    List<NaverNewsItemDTO> items;
}
