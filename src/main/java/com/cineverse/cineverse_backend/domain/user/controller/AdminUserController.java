package com.cineverse.cineverse_backend.domain.user.controller;

import com.cineverse.cineverse_backend.domain.user.dto.request.AddUserRequestDTO;
import com.cineverse.cineverse_backend.domain.user.dto.request.SearchUserRequestDTO;
import com.cineverse.cineverse_backend.domain.user.dto.request.UserModifyRequestDTO;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserListResponseDTO;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserResponseDTO;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserSummaryResponseDTO;
import com.cineverse.cineverse_backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/user")
@Tag(
        name = "Admin User",
        description = "관리자 사용자 API"
)
public class AdminUserController {

    private final UserService userService;

    @Operation(summary = "사용자 요약 조회", description = "사용자 관련 정보 요약 조회")
    @GetMapping("/summary")
    public ResponseEntity<List<UserSummaryResponseDTO>> getUserSummary() {
        return ResponseEntity.ok(userService.getUserSummary());
    }

    @Operation(summary = "사용자 리스트 조회", description = "검색 조건으로 사용자 리스트 조회")
    @GetMapping
    public ResponseEntity<Page<UserListResponseDTO>> getUserList(
            SearchUserRequestDTO searchUserRequestDTO,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(searchUserRequestDTO, pageable));
    }

    @Operation(summary = "사용자 단건 조회", description = "userId로 사용자 단건 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }

    @Operation(summary = "회원 정보 수정", description = "UserStatus, Role 수정")
    @PutMapping("/{userId}")
    public ResponseEntity<Void> modifyUser(
            @PathVariable Long userId,
            @RequestBody UserModifyRequestDTO userModifyRequestDTO) {
        userService.modifyUserStatusAndRole(userId, userModifyRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 추가", description = "회원 계정 생성")
    @PostMapping
    public ResponseEntity<Void> addUser(@RequestBody AddUserRequestDTO addUserRequestDTO) {
        userService.addUser(addUserRequestDTO);
        return ResponseEntity.noContent().build();
    }

}
