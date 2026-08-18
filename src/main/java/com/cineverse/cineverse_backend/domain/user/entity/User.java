package com.cineverse.cineverse_backend.domain.user.entity;

import com.cineverse.cineverse_backend.domain.user.enums.Gender;
import com.cineverse.cineverse_backend.domain.user.enums.UserRole;
import com.cineverse.cineverse_backend.domain.user.enums.UserStatus;
import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(length = 30)
    private String nickname;

    @Column(length = 100)
    private String name;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.NONE;

    private LocalDate birthDate;

    @Column(length = 20)
    private String phoneNumber;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    private LocalDateTime lastLoginAt;

    private LocalDateTime deletedAt;

    private String customerKey; // 결제시 필요

    @Builder.Default
    private boolean subscribed = false; // 구독 여부

    // 로그인 시간 기록
    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    // 구독 중으로 변경
    public void changeSubscribe() {
        this.subscribed = true;
    }

    // 관리자 페이지에서 사용자 정보 변경 - userStatus, userRole 정도만 변경
    public void changeUserStatusAndUserRole(UserStatus status, UserRole role) {
        this.status = status;
        this.role = role;
    }

    // 비밀번호 변경
    public void changePassword(String password) {
        this.password = password;
    }

    // 사용자 정보 변경
    public void updateUser(String nickname, String name, Gender gender, LocalDate birthDate, String phoneNumber) {
        this.nickname = nickname;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }
}
