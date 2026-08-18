package com.cineverse.cineverse_backend.domain.content.entity;

import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "contentGenre",
uniqueConstraints = {
        @UniqueConstraint(name = "uk_content_genre", columnNames = {"content_id", "genre_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentGenre extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentGenreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;
}
