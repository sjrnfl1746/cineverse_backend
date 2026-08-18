package com.cineverse.cineverse_backend.domain.terms.service;

import com.cineverse.cineverse_backend.domain.terms.dto.response.TermsResponseDTO;

import java.util.List;

public interface TermsService {

    // 최신 약관동의 내역 조회
    List<TermsResponseDTO> getActiveTerms();
}
