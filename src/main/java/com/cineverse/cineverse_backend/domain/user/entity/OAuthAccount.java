package com.cineverse.cineverse_backend.domain.user.entity;

import com.cineverse.cineverse_backend.domain.user.enums.OAuthProvider;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(
        name = "oauth_accounts",
        uniqueConstraints = {
                @UniqueConstraint( // 중복방지 - provider + provider_id 중복 불가능
                        name = "uk_provider_provider_id",
                        columnNames = {"provider", "provider_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oauthAccountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;

    @Column(length = 100)
    private String providerId;

    @Column(length = 100)
    private String email;
}
