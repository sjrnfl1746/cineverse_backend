package com.cineverse.cineverse_backend.domain.content.controller;

import com.cineverse.cineverse_backend.domain.content.dto.response.GenreResponseDTO;
import com.cineverse.cineverse_backend.domain.content.service.genre.GenreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genre")
@Tag(
        name = "Genre",
        description = "장르 관리 API"
)
public class GenreController {

    private final GenreService genreService;

    // 장르 리스트 조회
    @GetMapping
    public ResponseEntity<List<GenreResponseDTO>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }
}
