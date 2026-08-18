package com.cineverse.cineverse_backend.domain.terms.repository;

import com.cineverse.cineverse_backend.domain.terms.entity.UserTerms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTermsRepository extends JpaRepository<UserTerms, Long> {
}
