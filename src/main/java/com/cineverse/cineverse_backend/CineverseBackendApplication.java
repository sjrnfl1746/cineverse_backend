package com.cineverse.cineverse_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class CineverseBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CineverseBackendApplication.class, args);
    }

}
