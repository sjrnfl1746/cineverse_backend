package com.cineverse.cineverse_backend.domain.user.controller;

import com.cineverse.cineverse_backend.domain.user.dto.request.ChangePasswordRequestDTO;
import com.cineverse.cineverse_backend.domain.user.dto.request.UserMyPageRequestDTO;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserAddressResponseDTO;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserMyPageResponseDTO;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserResponseDTO;
import com.cineverse.cineverse_backend.domain.user.service.UserService;
import com.cineverse.cineverse_backend.global.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(
        name = "User",
        description = "사용자 API"
)
public class UserController {

    private final UserService userService;

    // 사용자 정보 조회
    @Operation(
            summary = "사용자 정보 조회",
            description = "사용자 정보 조회"
    )
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        UserResponseDTO meResponseDTO = userService.me(customUserDetails.getUserId());

        return ResponseEntity.ok(meResponseDTO);
    }

    @Operation(summary = "사용자 관련 정보들 반환", description = "사용자 페이지에서 필요한 정보를 반환")
    @GetMapping("/myPage")
    public ResponseEntity<UserMyPageResponseDTO> getUserSummary(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = (userDetails.getUserId() != null) ? userDetails.getUserId() : null;

        return ResponseEntity.ok(userService.getUserMyPage(userId));
    }

    // 비밀번호 변경
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경")
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {

        Long userId = (userDetails.getUserId() != null) ? userDetails.getUserId() : null;

        userService.changePassword(userId, changePasswordRequestDTO);
        return ResponseEntity.noContent().build();
    }

    // 주소 정보 반환
    @Operation(summary = "사용자 주소 정보 반환", description = "현재 로그인한 사용자의 주소정보를 반환")
    @GetMapping("/address")
    public ResponseEntity<UserAddressResponseDTO> getUserAddress(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = (userDetails.getUserId() != null) ? userDetails.getUserId() : null;

        return ResponseEntity.ok(userService.getUserAddress(userId));
    }

    // 사용자 정보 변경
    @Operation(summary = "사용자 정보 및 주소 변경", description = "사용자 정보 및 주소 변경")
    @PutMapping
    public ResponseEntity<Void> modifyUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestBody UserMyPageRequestDTO userMyPageRequestDTO) {

        Long userId = (userDetails.getUserId() != null) ? userDetails.getUserId() : null;

        userService.updateUser(userId, userMyPageRequestDTO);

        return ResponseEntity.noContent().build();
    }
}
