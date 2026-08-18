package com.cineverse.cineverse_backend.domain.user.repository;

import com.cineverse.cineverse_backend.domain.user.dto.request.SearchUserRequestDTO;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserListResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<UserListResponseDTO> findByKeyword(SearchUserRequestDTO searchUserRequestDTO, Pageable pageable);
}
