package com.cineverse.cineverse_backend.domain.content.entity;

import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "genre")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Genre extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreId;

    private String genreCode;

    private String genreName;

    private Long sortOrder;
}
