package com.cineverse.cineverse_backend.global.util.cookie;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class CookieProperties {

    @Value("${cookie.secure}")
    private boolean secure;

    @Value("${cookie.same-site}")
    private String sameSite;

    @Value("${cookie.refresh-token-expiration-days}")
    private Long refreshTokenExpDays;
}
