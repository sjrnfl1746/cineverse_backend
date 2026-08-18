package com.cineverse.cineverse_backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEMA_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI cineverseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CINEVERSE API")
                        .description("CINEVERSE 백엔드 API 문서")
                        .version("V1"))
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(SECURITY_SCHEMA_NAME))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        SECURITY_SCHEMA_NAME,
                                        new SecurityScheme()
                                                .name(SECURITY_SCHEMA_NAME)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}
