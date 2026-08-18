package com.cineverse.cineverse_backend.domain.terms.service;

import com.cineverse.cineverse_backend.domain.terms.entity.Terms;
import com.cineverse.cineverse_backend.domain.terms.entity.UserTerms;
import com.cineverse.cineverse_backend.domain.terms.repository.TermsRepository;
import com.cineverse.cineverse_backend.domain.terms.repository.UserTermsRepository;
import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.global.exception.BusinessException;
import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTermsServiceImpl implements UserTermsService {

    private final TermsRepository termsRepository;
    private final UserTermsRepository userTermsRepository;

    @Override
    public void saveAgreements(User user, List<Long> agreedTermsIds) {

        // 필수약관들 Id 조회
        List<Long> requiredTermIds = termsRepository.findRequiredActiveTermIds();

        // 필수약관들 동의 여부 확인
        Boolean allRequiredAgreed = agreedTermsIds.containsAll(requiredTermIds);

        // 필수약관들을 전부 동의 하지 않은 경우
        if(!allRequiredAgreed) {
            throw new BusinessException(ErrorCode.REQUIRED_TERMS_NOT_AGREED);
        }

        // 사용자가 동의한 약관 조회
        List<Terms> agreedTermsList = termsRepository.findAllById(agreedTermsIds);

        // userTerms 생성
        List<UserTerms> userTermsList = agreedTermsList.stream()
                .map(terms -> UserTerms.builder()
                        .user(user)
                        .terms(terms)
                        .agreed(true)
                        .build())
                .toList();

        // 약관 저장
        userTermsRepository.saveAll(userTermsList);
    }
}
