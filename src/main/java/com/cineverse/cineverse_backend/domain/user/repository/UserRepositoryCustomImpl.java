package com.cineverse.cineverse_backend.domain.user.repository;

import com.cineverse.cineverse_backend.domain.user.dto.request.SearchUserRequestDTO;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserListResponseDTO;
import com.cineverse.cineverse_backend.domain.user.entity.QUser;
import com.cineverse.cineverse_backend.domain.user.enums.UserStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QUser user = QUser.user;

    @Override
    public Page<UserListResponseDTO> findByKeyword(SearchUserRequestDTO searchUserRequestDTO, Pageable pageable) {

        List<UserListResponseDTO> userList = queryFactory
                .select(Projections.fields(
                        UserListResponseDTO.class,
                        user.userId,
                        user.email,
                        user.nickname,
                        user.name,
                        user.gender,
                        user.birthDate,
                        user.phoneNumber,
                        user.role,
                        user.status,
                        user.createdAt,
                        user.updatedAt
                ))
                .from(user)
                .where(
                        keywordContains(searchUserRequestDTO),
                        user.status.ne(UserStatus.WITHDRAWN)
                )
                .orderBy(user.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(user.count())
                .from(user)
                .where(
                        keywordContains(searchUserRequestDTO),
                        user.status.ne(UserStatus.WITHDRAWN)
                )
                .fetchOne();

        return new PageImpl<>(
                userList,
                pageable,
                Optional.ofNullable(total).orElse(0L));
    }

    private BooleanExpression keywordContains(SearchUserRequestDTO searchUserRequestDTO) {

        if (searchUserRequestDTO == null) {
            return null;
        }

        String type = searchUserRequestDTO.getType(); // 조회 타입
        String keyword = searchUserRequestDTO.getKeyword(); // 키워드

        if (type == null || keyword == null || keyword.isBlank()) {
            return null;
        }

        String trimmedKeyword = keyword.trim();

        return switch (type) {
            case "name" -> user.name.containsIgnoreCase(trimmedKeyword);
            case "nickname" -> user.nickname.containsIgnoreCase(trimmedKeyword);
            case "email" -> user.email.containsIgnoreCase(trimmedKeyword);
            default -> null;
        };
    }
}
