package com.cineverse.cineverse_backend.domain.news.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class NaverNewsProperties {

    @Value("${naver.news.url}")
    private String url;

    @Value("${naver.news.client-id}")
    private String clientId;

    @Value("${naver.news.client-secret}")
    private String clientSecret;
}
