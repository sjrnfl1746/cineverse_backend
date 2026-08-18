package com.cineverse.cineverse_backend.domain.auth.service;

import com.cineverse.cineverse_backend.domain.auth.dto.request.AddressRequestDTO;
import com.cineverse.cineverse_backend.domain.auth.dto.request.LoginRequestDTO;
import com.cineverse.cineverse_backend.domain.auth.dto.request.SignupRequestDTO;
import com.cineverse.cineverse_backend.domain.auth.dto.response.*;
import com.cineverse.cineverse_backend.domain.mail.enums.EmailVerificationType;
import com.cineverse.cineverse_backend.domain.mail.service.MailService;
import com.cineverse.cineverse_backend.domain.terms.service.UserTermsService;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserResponseDTO;
import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.domain.user.entity.UserAddress;
import com.cineverse.cineverse_backend.domain.user.enums.UserStatus;
import com.cineverse.cineverse_backend.domain.user.repository.UserAddressRepository;
import com.cineverse.cineverse_backend.domain.user.repository.UserRepository;
import com.cineverse.cineverse_backend.global.exception.BusinessException;
import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import com.cineverse.cineverse_backend.global.security.jwt.JwtProvider;
import com.cineverse.cineverse_backend.global.security.jwt.RefreshTokenInfo;
import com.cineverse.cineverse_backend.global.security.jwt.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserTermsService userTermsService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    @Override
    public void signup(SignupRequestDTO signupRequestDTO) {

        // 증복이 불가능한 status
        List<UserStatus> unavailableStatuses = List.of(UserStatus.ACTIVE, UserStatus.SUSPENDED);

        // 이메일 인증 여부 확인
        if (!mailService.isVerified(signupRequestDTO.getEmail(), EmailVerificationType.SIGNUP)) {
            throw new BusinessException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        // email 중복 확인
        if (userRepository.existsByEmailAndStatusIn(signupRequestDTO.getEmail(), unavailableStatuses)) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 닉네임 중복 확인
        if (userRepository.existsByNicknameAndStatusIn(signupRequestDTO.getNickname(), unavailableStatuses)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // user 저장
        User user = User.builder()
                .email(signupRequestDTO.getEmail())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .nickname(signupRequestDTO.getNickname())
                .name(signupRequestDTO.getName())
                .gender(signupRequestDTO.getGender())
                .birthDate(signupRequestDTO.getBirthDate())
                .phoneNumber(signupRequestDTO.getPhoneNumber())
                .customerKey(UUID.randomUUID().toString())
                .build();

        userRepository.save(user);

        // address 저장
        AddressRequestDTO address = signupRequestDTO.getAddress();

        UserAddress userAddress = UserAddress.builder()
                .user(user)
                .zipCode(address.getZipCode())
                .city(address.getCity())
                .district(address.getDistrict())
                .street(address.getStreet())
                .detail(address.getDetail())
                .primaryAddress(true)
                .build();

        userAddressRepository.save(userAddress);

        // terms 저장
        userTermsService.saveAgreements(user, signupRequestDTO.getAgreedTermsIds());

        // redis에 저장된 이메일 인증 여부 삭제
        mailService.deleteVerified(signupRequestDTO.getEmail(), EmailVerificationType.SIGNUP);
    }

    @Transactional
    @Override
    public LoginResultDTO login(LoginRequestDTO loginRequestDTO) {

        // 이메일 검증
        User user = userRepository.findByEmailAndStatus(loginRequestDTO.getEmail(), UserStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));

        // 비밀번호 검증
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        // 로그인 시간 저장
        user.updateLastLoginAt();

        // accessToken
        String accessToken = jwtProvider.generateAccessToken(user.getUserId(), user.getEmail());

        // refreshToken 생성
        String refreshToken = jwtProvider.generateRefreshToken();

        // redis에 저장
        refreshTokenService.saveRefreshToken(refreshToken, user.getUserId(), loginRequestDTO.getRememberMe());;

        // user 정보
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .lastLoginAt(user.getLastLoginAt())
                .subscribed(user.isSubscribed())
                .build();

        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                .accessToken(accessToken)
                .subscribed(user.isSubscribed())
                .user(userResponseDTO)
                .build();

        return LoginResultDTO.builder()
                .refreshToken(refreshToken)
                .response(loginResponseDTO)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) { // StringUtils.hasText(): null, "", " " 전부 확인
            return;
        }
        refreshTokenService.deleteRefreshToken(refreshToken);
    }

    @Transactional
    @Override
    public TokenResultDTO refreshToken(String refreshToken) {

        // 토큰이 존재하는지 확인
        if (!StringUtils.hasText(refreshToken)) {
            throw new BusinessException(ErrorCode.TOKEN_NOT_FOUND);
        }

        // refreshToken 으로 refreshTokenInfo 조회
        RefreshTokenInfo info = refreshTokenService.getRefreshTokenInfo(refreshToken);


        // 사용자 정보 조회
        User user = userRepository.findByUserIdAndStatus(info.getUserId(), UserStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // accessToken, refreshToken 재발급 - RTR 방식
        String newAccessToken = jwtProvider.generateAccessToken(info.getUserId(), user.getEmail());
        String newRefreshToken = jwtProvider.generateRefreshToken();

        // 기존 토큰 삭제 후 새로운 토큰 저장
        refreshTokenService.deleteRefreshToken(refreshToken);
        refreshTokenService.saveRefreshToken(newRefreshToken, user.getUserId(), info.isRememberMe());

        TokenResponseDTO tokenResponseDTO = TokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .build();

        return TokenResultDTO.builder()
                .tokenResponseDTO(tokenResponseDTO)
                .refreshToken(newRefreshToken)
                .rememberMe(info.isRememberMe())
                .build();
    }
}
