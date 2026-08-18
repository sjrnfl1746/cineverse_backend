package com.cineverse.cineverse_backend.domain.mail.controller;

import com.cineverse.cineverse_backend.domain.mail.dto.EmailCodeSendRequestDTO;
import com.cineverse.cineverse_backend.domain.mail.dto.EmailCodeVerifyRequestDTO;
import com.cineverse.cineverse_backend.domain.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
@Tag(
        name = "Mail",
        description = "메일 API"
)
public class MailController {

    private final MailService mailService;

    // 이메일 인증번호 발송
    @Operation(
            summary = "이메일 인증번호 발송",
            description = "이메일 인증번호 발송"
    )
    @PostMapping("/code/send")
    public ResponseEntity<Void> sendEmailCode(@Valid @RequestBody EmailCodeSendRequestDTO emailCodeSendRequestDTO) {
        mailService.sendEmailCode(emailCodeSendRequestDTO);
        return ResponseEntity.ok().build();
    }

    // 이메일 인증번호 확인
    @Operation(
            summary = "이메일 인증번호 확인",
            description = "이메일 인증번호 확인"
    )
    @PostMapping("/code/verify")
    public ResponseEntity<Void> verifyEmailCode(@Valid @RequestBody EmailCodeVerifyRequestDTO emailCodeVerifyRequestDTO) {
        mailService.verifyEmailCode(emailCodeVerifyRequestDTO);
        return ResponseEntity.ok().build();
    }
}
