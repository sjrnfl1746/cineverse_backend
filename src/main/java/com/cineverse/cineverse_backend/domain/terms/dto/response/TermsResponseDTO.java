package com.cineverse.cineverse_backend.domain.terms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermsResponseDTO {

    private Long termsId;

    private String title;

    private String content;

    private String type;

    private Boolean required;

    private String version;

    private Boolean active;

    private Integer sortOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
