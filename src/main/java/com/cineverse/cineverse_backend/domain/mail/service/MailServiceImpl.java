package com.cineverse.cineverse_backend.domain.mail.service;

import com.cineverse.cineverse_backend.domain.mail.dto.EmailCodeSendRequestDTO;
import com.cineverse.cineverse_backend.domain.mail.dto.EmailCodeVerifyRequestDTO;
import com.cineverse.cineverse_backend.domain.mail.enums.EmailVerificationType;
import com.cineverse.cineverse_backend.domain.user.enums.UserStatus;
import com.cineverse.cineverse_backend.domain.user.repository.UserRepository;
import com.cineverse.cineverse_backend.global.exception.BusinessException;
import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender javaMailSender;

    @Value("${auth.email.code-expiration-minutes}")
    private Long codeExpMinutes; // 인증번호 유효시간

    @Value("${auth.email.verified-expiration-minutes}")
    private Long verifiedExpMinutes; // 인증완료 유효시간

    @Override
    public void sendEmailCode(EmailCodeSendRequestDTO emailCodeSendRequestDTO) {

        String email = emailCodeSendRequestDTO.getEmail(); // 이메일
        EmailVerificationType type = emailCodeSendRequestDTO.getType(); // 인증 타입

        // 유효성 검사
        validateEmailByType(email, type);

        // 인증번호 생성 - 인증번호는 6자리 숫자
        String code = generateCode();

        // redis에 저장
        saveCode(email, type, code);

        // 메읿 발송
        sendCodeMail(email, type, code);
    }

    @Override
    public void verifyEmailCode(EmailCodeVerifyRequestDTO emailCodeVerifyRequestDTO) {

        // 받은 값들
        String email = emailCodeVerifyRequestDTO.getEmail();
        String code = emailCodeVerifyRequestDTO.getCode();
        EmailVerificationType type = emailCodeVerifyRequestDTO.getType();

        // 저장된 값들
        String key = getCodeKey(email, type);
        String savedCode = redisTemplate.opsForValue().get(key);

        // 유효성 검사
        if (savedCode == null) { // 저장된 코드가 존재 X
            throw new BusinessException(ErrorCode.EXPIRED_EMAIL_CODE);
        }
        if (!savedCode.equals(code)) { // 저장된 코드와 받은 코드가 일치 X
            throw new BusinessException(ErrorCode.INVALID_EMAIL_CODE);
        }

        // 인증이 완료되면 redis에 저장된 값 삭제
        redisTemplate.delete(key);

        // 인증완료 여부 저장
        String verifiedKey = getVerifiedKey(email, type);
        redisTemplate.opsForValue().set(verifiedKey, "true", Duration.ofMinutes(verifiedExpMinutes));
    }

    @Override
    public Boolean isVerified(String email, EmailVerificationType type) {

        String key = getVerifiedKey(email, type);

        Boolean isVerified = redisTemplate.hasKey(key);

        return isVerified;
    }

    @Override
    public void deleteVerified(String email, EmailVerificationType type) {

        String key = getVerifiedKey(email, type);

        redisTemplate.delete(key);
    }

    /* 인증 타입별 이메일 검증 */
    private void validateEmailByType(String email, EmailVerificationType type) {

        // 증복이 불가능한 status
        List<UserStatus> unavailableStatuses = List.of(UserStatus.ACTIVE, UserStatus.SUSPENDED);

        // 회원가입인 경우
        if (type == EmailVerificationType.SIGNUP) {
            if (userRepository.existsByEmailAndStatusIn(email, unavailableStatuses)) { // 이메일이 존재하는 경우
                throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
            }
        }

        // 비밀번호 재설정인 경우
        if (type == EmailVerificationType.PASSWORD_RESET) {
            if (!userRepository.existsByEmailAndStatusIn(email, unavailableStatuses)) { // 가입되지 않은 이메일인 경우
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }
        }
    }

    /* 6자리 인증번호 생성 */
    private String generateCode() {
        Random random = new Random();
        String code = String.format("%06d", random.nextInt(1_000_000));
        return code;
    }

    /* Redis에 인증번호 저장 */
    private void saveCode(String email, EmailVerificationType type, String code) {
        String key = getCodeKey(email, type);

        redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(codeExpMinutes));
    }

    /* Redis key 생성 */
    private String getCodeKey(String email, EmailVerificationType type) {
        String key = "EMAIL_CODE:" + type + ":" + email;
        return key;
    }

    /* Redis 인증완료 여부 저장 key 생성 */
    private String getVerifiedKey(String email, EmailVerificationType type) {
        String key = "EMAIL_VERIFIED:" + type + ":" + email;
        return key;
    }

    /* 메일 발송 */
    private void sendCodeMail(String email, EmailVerificationType type, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name()); // false - 첨부파일 사용 X

            helper.setTo(email);
            helper.setSubject(getSubject(type));
            helper.setText(getHtml(type, code), true); // true - html 파일

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 발송에 실패했습니다.", e);
        }
    }

    /* 메일 제목 생성 */
    private String getSubject(EmailVerificationType type) {
        return switch (type) {
            case SIGNUP -> "[CINEVERSE] 이메일 인증번호";
            case PASSWORD_RESET -> "[CINEVERSE] 비밀번호 재설정 인증번호";
        };
    }

    /* 메일 내용 생성 */
    private String getHtml(EmailVerificationType type, String code) {

        // 제목
        String title = switch (type) {
            case SIGNUP -> "[CINEVERSE] 이메일 인증";
            case PASSWORD_RESET -> "[CINEVERSE] 비밀번호 재설정";
        };

        // 메시지
        String message = switch (type) {
            case SIGNUP -> "회원가입을 위해 아래 인증번호를 입력해주세요.";
            case PASSWORD_RESET -> "비밀번호 재설정을 위해 아래 인증번호를 입력해주세요.";
        };

        // html
        String html = """
                <div style="font-family: Arial, sans-serif;">
                    <h2>%s</h2>
                    <p>%s</p>
                    <h1 style="letter-spacing: 4px;">%s</h1>
                    <p>인증번호는 %d분간 유효합니다.</p>
                </div>
                """.formatted(title, message, code, codeExpMinutes);

        return html;
    }
}
