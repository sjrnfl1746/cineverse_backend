package com.cineverse.cineverse_backend.domain.user.repository;

import com.cineverse.cineverse_backend.domain.user.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    // userId로 주소 조회
    Optional<UserAddress> findByUser_UserId(Long userId);
}
