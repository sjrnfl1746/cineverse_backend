package com.cineverse.cineverse_backend.domain.terms.repository;

import com.cineverse.cineverse_backend.domain.terms.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TermsRepository extends JpaRepository<Terms, Long> {

    List<Terms> findByActiveTrueOrderBySortOrderAsc();

    // active가 true인 필수 약관들
    @Query("select t.termsId from Terms t where t.required = true and t.active = true")
    List<Long> findRequiredActiveTermIds();
}
