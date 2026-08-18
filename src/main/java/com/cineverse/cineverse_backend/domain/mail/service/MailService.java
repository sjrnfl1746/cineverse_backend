package com.cineverse.cineverse_backend.domain.mail.service;

import com.cineverse.cineverse_backend.domain.mail.dto.EmailCodeSendRequestDTO;
import com.cineverse.cineverse_backend.domain.mail.dto.EmailCodeVerifyRequestDTO;
import com.cineverse.cineverse_backend.domain.mail.enums.EmailVerificationType;

public interface MailService {

    // 이메일 코드 전송
    void sendEmailCode(EmailCodeSendRequestDTO emailCodeSendRequestDTO);

    // 인증번호 검증
    void verifyEmailCode(EmailCodeVerifyRequestDTO emailCodeVerifyRequestDTO);

    // 검증여부 확인
    Boolean isVerified(String email, EmailVerificationType type);

    // 검증 삭제
    void deleteVerified(String email, EmailVerificationType type);
}
