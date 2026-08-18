package com.cineverse.cineverse_backend.domain.news.controller;

import com.cineverse.cineverse_backend.domain.news.dto.NaverNewsResponseDTO;
import com.cineverse.cineverse_backend.domain.news.service.NaverNewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
@Tag(
        name = "News",
        description = "뉴스 관련 API"
)
public class NewsController {

    private final NaverNewsService naverNewsService;

    @Operation(summary = "네이버 뉴스 반환", description = "영화 관련된 뉴스 반환")
    @GetMapping
    public ResponseEntity<NaverNewsResponseDTO> getNewsList(@RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(naverNewsService.getNaverNews(size));
    }
}
