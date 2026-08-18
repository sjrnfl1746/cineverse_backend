package com.cineverse.cineverse_backend.domain.content.repository.userWishlist;

import com.cineverse.cineverse_backend.domain.content.entity.QContent;
import com.cineverse.cineverse_backend.domain.image.entity.QImage;
import com.cineverse.cineverse_backend.domain.image.enums.ImageTargetType;
import com.cineverse.cineverse_backend.domain.image.enums.ImageType;
import com.cineverse.cineverse_backend.domain.user.dto.response.UserWishlistResponseDTO;
import com.cineverse.cineverse_backend.domain.user.entity.QUserWishlist;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserWishlistRepositoryCustomImpl implements UserWishlistRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QUserWishlist userWishlist = QUserWishlist.userWishlist;
    private final QImage image = QImage.image;
    private final QContent content = QContent.content;

    @Transactional
    @Override
    public Page<UserWishlistResponseDTO> findAllUserWishlist(Long userId, Pageable pageable) {

        List<UserWishlistResponseDTO> userWishlistResponseDTOList = queryFactory
                .select(Projections.fields(
                        UserWishlistResponseDTO.class,
                        userWishlist.userWishlistId,
                        content.contentId,
                        content.title.as("contentTitle"),
                        image.path.as("posterUrl"),
                        content.releaseAt
                ))
                .from(userWishlist)
                .leftJoin(content)
                .on(
                        userWishlist.content.contentId.eq(content.contentId)
                )
                .leftJoin(image)
                .on(
                        image.targetId.eq(content.contentId),
                        image.targetType.eq(ImageTargetType.CONTENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )
                .where(
                        userWishlist.user.userId.eq(userId)
                )
                .orderBy(
                        userWishlist.createdAt.desc(),
                        userWishlist.userWishlistId.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(userWishlist.count())
                .from(userWishlist)
                .where(
                        userWishlist.user.userId.eq(userId)
                )
                .fetchOne();

        return new PageImpl<>(userWishlistResponseDTOList, pageable, Optional.ofNullable(total).orElse(0L));
    }
}
