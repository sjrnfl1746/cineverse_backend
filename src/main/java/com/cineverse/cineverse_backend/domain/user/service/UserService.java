package com.cineverse.cineverse_backend.domain.user.service;

import com.cineverse.cineverse_backend.domain.user.dto.request.*;
import com.cineverse.cineverse_backend.domain.dashboard.dto.SummaryDTO;
import com.cineverse.cineverse_backend.domain.user.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    // 내 정보 반환
    UserResponseDTO me(Long userId);

    // 전체 회원 수 요약
    SummaryDTO getAllUserCount();

    // 최근 가입한 회원 - 4명
    List<UserDashboardResponseDTO> getAllUserDashboard();

    // 구독한 전체 회원 수
    SummaryDTO countSubscribedUsers();

    // 사용자 리스트 조회
    Page<UserListResponseDTO> getAllUsers(SearchUserRequestDTO searchUserRequestDTO, Pageable pageable);

    // 사용자 단건 조회
    UserResponseDTO getUserByUserId(Long userId);

    // 사용자 status, role 변경
    void modifyUserStatusAndRole(Long userId, UserModifyRequestDTO userModifyRequestDTO);

    // 사용자 요약 정보 조회
    List<UserSummaryResponseDTO> getUserSummary();

    // 계정 추가
    void addUser(AddUserRequestDTO addUserRequestDTO);

    // 사용자 페이지 요약 정보 조회
    UserMyPageResponseDTO getUserMyPage(Long userId);

    // 비밀번호 변경
    void changePassword(Long userId, ChangePasswordRequestDTO changePasswordRequestDTO);

    // 사용자 주소 조회
    UserAddressResponseDTO getUserAddress(Long userId);

    // 사용자 정보 수정
    void updateUser(Long userId, UserMyPageRequestDTO userMyPageRequestDTO);
}
