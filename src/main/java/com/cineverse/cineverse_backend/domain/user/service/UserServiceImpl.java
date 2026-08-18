package com.cineverse.cineverse_backend.domain.user.service;

import com.cineverse.cineverse_backend.domain.user.dto.request.*;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewListResponseDTO;
import com.cineverse.cineverse_backend.domain.content.repository.review.ContentReviewRepository;
import com.cineverse.cineverse_backend.domain.content.repository.userWishlist.UserWishlistRepository;
import com.cineverse.cineverse_backend.domain.dashboard.dto.SummaryDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.SubscriptionPlanResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.entity.Subscription;
import com.cineverse.cineverse_backend.domain.subscription.entity.SubscriptionPlan;
import com.cineverse.cineverse_backend.domain.subscription.enums.SubscriptionStatus;
import com.cineverse.cineverse_backend.domain.subscription.repository.subscription.SubscriptionPlanRepository;
import com.cineverse.cineverse_backend.domain.subscription.repository.subscription.SubscriptionRepository;
import com.cineverse.cineverse_backend.domain.user.dto.response.*;
import com.cineverse.cineverse_backend.domain.user.entity.User;
import com.cineverse.cineverse_backend.domain.user.entity.UserAddress;
import com.cineverse.cineverse_backend.domain.user.enums.UserStatus;
import com.cineverse.cineverse_backend.domain.user.repository.UserAddressRepository;
import com.cineverse.cineverse_backend.domain.user.repository.UserRepository;
import com.cineverse.cineverse_backend.global.exception.BusinessException;
import com.cineverse.cineverse_backend.global.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final UserWishlistRepository userWishlistRepository;
    private final ContentReviewRepository contentReviewRepository;
    private final UserAddressRepository userAddressRepository;

    @Transactional
    @Override
    public UserResponseDTO me(Long userId) {

        // user 정보 조회
        User user = userRepository.findByUserIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .subscribed(user.isSubscribed())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Transactional
    @Override
    public SummaryDTO getAllUserCount() {

        return SummaryDTO.builder()
                .title("전체 회원")
                .value(userRepository.count())
                .money(false)
                .build();
    }

    @Override
    public List<UserDashboardResponseDTO> getAllUserDashboard() {
        return userRepository.findTop4ByOrderByCreatedAtDesc()
                .stream().map(user -> UserDashboardResponseDTO.builder()
                        .userId(user.getUserId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .createdAt(user.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    public SummaryDTO countSubscribedUsers() {
        return SummaryDTO.builder()
                .title("구독 회원")
                .value(userRepository.countByStatusInAndSubscribedTrue(List.of(UserStatus.ACTIVE, UserStatus.SUSPENDED)))
                .money(false)
                .build();
    }

    @Override
    public Page<UserListResponseDTO> getAllUsers(SearchUserRequestDTO searchUserRequestDTO, Pageable pageable) {
        return userRepository.findByKeyword(searchUserRequestDTO, pageable);
    }

    @Transactional
    @Override
    public UserResponseDTO getUserByUserId(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .subscribed(user.isSubscribed())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Transactional
    @Override
    public void modifyUserStatusAndRole(Long userId, UserModifyRequestDTO userModifyRequestDTO) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (userModifyRequestDTO.getRole() == null) {
            userModifyRequestDTO.setRole(user.getRole());
        }

        if (userModifyRequestDTO.getStatus() == null) {
            userModifyRequestDTO.setStatus(user.getStatus());
        }

        user.changeUserStatusAndUserRole(userModifyRequestDTO.getStatus(), userModifyRequestDTO.getRole());
    }

    @Transactional
    @Override
    public List<UserSummaryResponseDTO> getUserSummary() {

        List<UserSummaryResponseDTO> userSummaryResponseDTOList = new ArrayList<>();

        // 전체 회원 - 탈퇴회원 제외
        Long totalUser = userRepository.countByStatusNotIn(List.of(UserStatus.WITHDRAWN));
        UserSummaryResponseDTO totalUserDTO = UserSummaryResponseDTO.builder()
                .title("전체 회원")
                .value(totalUser)
                .build();
        userSummaryResponseDTOList.add(totalUserDTO);

        // 오늘 가입
        LocalDate today = LocalDate.now();
        Long todayUser = userRepository.countByCreatedAtGreaterThanEqualAndCreatedAtLessThanAndStatusNot(today.atStartOfDay(),
                today.plusDays(1).atStartOfDay(), UserStatus.WITHDRAWN);
        UserSummaryResponseDTO todayUserDTO = UserSummaryResponseDTO.builder()
                .title("오늘 가입")
                .value(todayUser)
                .build();
        userSummaryResponseDTOList.add(todayUserDTO);

        // 활성 회원
        Long activeUser = userRepository.countByStatusIn(List.of(UserStatus.ACTIVE));
        UserSummaryResponseDTO activeUserDTO = UserSummaryResponseDTO.builder()
                .title("활성 회원")
                .value(activeUser)
                .build();
        userSummaryResponseDTOList.add(activeUserDTO);

        // 이용 정지
        Long suspendedUser = userRepository.countByStatusIn(List.of(UserStatus.SUSPENDED));
        UserSummaryResponseDTO suspendedUserDTO = UserSummaryResponseDTO.builder()
                .title("이용 정지")
                .value(suspendedUser)
                .build();
        userSummaryResponseDTOList.add(suspendedUserDTO);

        return userSummaryResponseDTOList;
    }

    @Override
    public void addUser(AddUserRequestDTO addUserRequestDTO) {

        // 증복이 불가능한 status
        List<UserStatus> unavailableStatuses = List.of(UserStatus.ACTIVE, UserStatus.SUSPENDED);

        // email 중복 확인
        if (userRepository.existsByEmailAndStatusIn(addUserRequestDTO.getEmail(), unavailableStatuses)) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 닉네임 중복 확인
        if (userRepository.existsByNicknameAndStatusIn(addUserRequestDTO.getNickname(), unavailableStatuses)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        User user = User.builder()
                .email(addUserRequestDTO.getEmail())
                .password(passwordEncoder.encode(addUserRequestDTO.getPassword()))
                .nickname(addUserRequestDTO.getNickname())
                .name(addUserRequestDTO.getName())
                .gender(addUserRequestDTO.getGender())
                .birthDate(LocalDate.now())
                .phoneNumber(addUserRequestDTO.getPhoneNumber())
                .customerKey(UUID.randomUUID().toString())
                .role(addUserRequestDTO.getRole())
                .build();
        userRepository.save(user);
    }

    @Transactional
    @Override
    public UserMyPageResponseDTO getUserMyPage(Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // ***** 구독 정보 조회 *****
        // 구독 조회
        Subscription subscription = subscriptionRepository.findByUser_UserIdAndStatus(userId, SubscriptionStatus.ACTIVE);

        // 구독중인 플랜 정보 조회
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(subscription.getSubscriptionPlan().getSubscriptionPlanId())
                .orElseThrow(() -> new RuntimeException("구독 플랜이 존재하지 않습니다."));

        SubscriptionPlanResponseDTO subscriptionPlanResponseDTO = SubscriptionPlanResponseDTO.builder()
                .subscriptionPlanId(subscriptionPlan.getSubscriptionPlanId())
                .code(subscriptionPlan.getCode())
                .name(subscriptionPlan.getName())
                .amount(subscriptionPlan.getAmount())
                .billingCycleMonths(subscriptionPlan.getBillingCycleMonths())
                .subscribed(true)
                .currentPeriodEndAt(subscription.getStartedAt().plusMonths(subscriptionPlan.getBillingCycleMonths()))
                .build();

        // ***** 작성 리뷰, 찜한 콘텐츠 *****
        List<UserSummaryResponseDTO> userSummaryResponseDTOList = new ArrayList<>();

        // 작성 리뷰
        Long reviewlistCount = contentReviewRepository.countByUser_UserId(userId);
        UserSummaryResponseDTO reviewlist = UserSummaryResponseDTO.builder()
                .title("작성한 리뷰")
                .value(reviewlistCount)
                .build();
        userSummaryResponseDTOList.add(reviewlist);

        // 찜한 콘텐츠
        Long userWishlistCount = userWishlistRepository.countByUser_UserId(userId);
        UserSummaryResponseDTO wishlist = UserSummaryResponseDTO.builder()
                .title("찜한 콘텐츠")
                .value(userWishlistCount)
                .build();
        userSummaryResponseDTOList.add(wishlist);

        // ***** 최근 작성 리뷰 3개 *****
        List<ContentReviewListResponseDTO> contentReviewList = contentReviewRepository.findContentReviewByUserId(userId);

        return UserMyPageResponseDTO.builder()
                .subscriptionPlan(subscriptionPlanResponseDTO)
                .userSummaryList(userSummaryResponseDTOList)
                .contentReviews(contentReviewList)
                .build();
    }

    @Transactional
    @Override
    public void changePassword(Long userId, ChangePasswordRequestDTO changePasswordRequestDTO) {

        // 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        boolean isCorrectPassword = passwordEncoder.matches(changePasswordRequestDTO.getCurrentPassword(), user.getPassword());

        if (!isCorrectPassword) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String encodePassword = passwordEncoder.encode(changePasswordRequestDTO.getNewPassword());

        user.changePassword(encodePassword);
    }

    @Transactional
    @Override
    public UserAddressResponseDTO getUserAddress(Long userId) {

        // 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 주소 조회
        UserAddress userAddress = userAddressRepository.findByUser_UserId(userId).orElseThrow(
                () -> new RuntimeException("주소가 존재하지 않습니다."));

        return UserAddressResponseDTO.builder()
                .addressId(userAddress.getAddressId())
                .zipCode(userAddress.getZipCode())
                .city(userAddress.getCity())
                .district(userAddress.getDistrict())
                .street(userAddress.getStreet())
                .detail(userAddress.getDetail())
                .build();
    }

    @Transactional
    @Override
    public void updateUser(Long userId, UserMyPageRequestDTO userMyPageRequestDTO) {

        // 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 주소 조회
        UserAddress userAddress = userAddressRepository.findByUser_UserId(userId).orElseThrow(
                () -> new RuntimeException("주소가 존재하지 않습니다."));

        // 사용자 정보 수정
        user.updateUser(userMyPageRequestDTO.getNickname(), userMyPageRequestDTO.getName(), userMyPageRequestDTO.getGender(),
                userMyPageRequestDTO.getBirthDate(), userMyPageRequestDTO.getPhoneNumber());

        // 주소 수정
        userAddress.updateAddress(userMyPageRequestDTO.getZipCode(), userMyPageRequestDTO.getCity(), userMyPageRequestDTO.getDistrict(),
                userMyPageRequestDTO.getStreet(), userMyPageRequestDTO.getDetail());
    }
}
