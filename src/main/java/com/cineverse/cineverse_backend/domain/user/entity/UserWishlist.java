package com.cineverse.cineverse_backend.domain.user.entity;

import com.cineverse.cineverse_backend.domain.content.entity.Content;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "user_wishlist",
uniqueConstraints = {
        @UniqueConstraint(name = "uk_wishlist_user_content",
        columnNames = {"user_id", "content_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserWishlist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userWishlistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;
}
