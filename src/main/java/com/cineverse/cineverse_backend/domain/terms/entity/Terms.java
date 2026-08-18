package com.cineverse.cineverse_backend.domain.terms.entity;

import com.cineverse.cineverse_backend.domain.terms.enums.TermsType;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "terms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Terms extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termsId;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private TermsType type;

    @Builder.Default
    private Boolean required = false;

    @Column(length = 20)
    private String version;

    private Boolean active;

    private Integer sortOrder;
}
