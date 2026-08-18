package com.cineverse.cineverse_backend.domain.user.repository;

import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.domain.user.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    // 탈퇴한 회원이 아닌 계정 중 email 중복 있는지 확인
    Boolean existsByEmailAndStatusIn(String email, Collection<UserStatus> status);

    // 탈퇴한 회원이 아닌 계정 중 nickname 중복 있는지 확인
    Boolean existsByNicknameAndStatusIn(String nickname, Collection<UserStatus> status);

    // userId, userStatus로 user 조회
    Optional<User> findByUserIdAndStatus(Long userId, UserStatus status);

    // email, userStatus로 user 조회
    Optional<User> findByEmailAndStatus(String email, UserStatus status);

    // 최근 가입한 회원 4명 반환
    List<User> findTop4ByOrderByCreatedAtDesc();

    // 구독한 회원의 전체 수
    Long countByStatusInAndSubscribedTrue(Collection<UserStatus> statuses);

    // 탈퇴 회원을 제외한 전체 회원 수
    Long countByStatusNotIn(Collection<UserStatus> statuses);

    // 오늘 가입한 회원 수 - 탈퇴회원 제외
    Long countByCreatedAtGreaterThanEqualAndCreatedAtLessThanAndStatusNot(LocalDateTime start, LocalDateTime end, UserStatus status);

    // userStatus가 해당되는 회원 수
    Long countByStatusIn(Collection<UserStatus> statuses);

}
