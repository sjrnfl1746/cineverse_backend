package com.cineverse.cineverse_backend.domain.news.service;

import com.cineverse.cineverse_backend.domain.news.dto.NaverNewsResponseDTO;
import com.cineverse.cineverse_backend.domain.news.properties.NaverNewsProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NaverNewsService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public NaverNewsService(NaverNewsProperties naverNewsProperties, ObjectMapper objectMapper) {
        this.restClient = RestClient.builder()
                .baseUrl(naverNewsProperties.getUrl())
                .defaultHeader("X-NCP-APIGW-API-KEY-ID", naverNewsProperties.getClientId())
                .defaultHeader("X-NCP-APIGW-API-KEY", naverNewsProperties.getClientSecret())
                .build();

        this.objectMapper = objectMapper;
    }

    public NaverNewsResponseDTO getNaverNews(int size) {

        // 크기 조절 - 보낸값이 20 이상이어도 1 ~ 20개까지로 조절
        int safeSize = Math.min(Math.max(size, 1), 20);

        String responseBody = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", "영화")
                        .queryParam("display", safeSize)
                        .queryParam("start", 1)
                        .queryParam("sort", "date")
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(String.class);

        if (responseBody == null || responseBody.isBlank()) {
            throw new IllegalStateException("네이버 뉴스 API 응답이 비어 있습니다.");
        }

        try {
            return objectMapper.readValue(responseBody, NaverNewsResponseDTO.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("네이버 뉴스 API 응답을 반환하지 못했습니다.", e);
        }
    }
}
