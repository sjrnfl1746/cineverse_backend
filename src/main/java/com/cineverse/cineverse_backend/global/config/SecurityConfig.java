package com.cineverse.cineverse_backend.global.config;

import com.cineverse.cineverse_backend.global.security.jwt.JwtAuthenticationFilter;
import com.cineverse.cineverse_backend.global.security.jwt.JwtProvider;
import com.cineverse.cineverse_backend.global.security.user.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    private final String[] PERMIT_URLS = {
            // Swagger
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",

            "/api/auth/**",
            "/api/terms/**",
            "/api/mail/**",
            "/api/genre/**",
            "/api/content/**",
            "/api/review/**",
            "/api/news/**",
            "/api/event/**",
            "/api/eventAnnounce/**",
            "/api/notice/**",

            "/uploads/images/**",
            "/uploads/videos/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(PERMIT_URLS).permitAll()
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )

                // 인증이 안 된 사용자가 인증이 필요한 API에 접근 시 401로 응답해라 - 로그인 X -> 401로 반환
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        }))

                // 필터 등록
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtProvider, customUserDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                )

                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5173"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
