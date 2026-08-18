package com.cineverse.cineverse_backend.domain.terms.service;

import com.cineverse.cineverse_backend.domain.user.entity.User;

import java.util.List;

public interface UserTermsService {

    // 약관동의 내역 저장
    void saveAgreements(User user, List<Long> agreedTermsIds);
}
