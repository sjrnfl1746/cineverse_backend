package com.cineverse.cineverse_backend.global.security.user;

import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.domain.user.enums.UserStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    // userId 추출
    public Long getUserId() {
        return user.getUserId();
    }

    // 이메일 추출
    public String getEmail() {
        return user.getEmail();
    }

    // 닉네임 추출
    public String getNickname() {
        return user.getNickname();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    // 비밀번호 추출
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 식별자 추출 - username은 식별자 - 현재 email을 식별자로 사용하므로 email 반환
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // 계정 만료 여부 - true: 만료되지 않음
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부 - userStatus가 Suspended 인지 확인
    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() == UserStatus.SUSPENDED;
    }

    // 비밀번호 만료 여부 - 사용 x
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 사용가능한 계정인지 여부 - userStatus가 active 인지 확인
    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.ACTIVE;
    }
}
