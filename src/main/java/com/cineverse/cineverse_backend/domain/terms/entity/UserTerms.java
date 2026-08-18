package com.cineverse.cineverse_backend.domain.terms.entity;

import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(
        name = "user_terms",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_terms_user_terms",
                        columnNames = {"user_id", "terms_id"}
                )
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserTerms extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTermsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id")
    private Terms terms;

    private Boolean agreed;
}
