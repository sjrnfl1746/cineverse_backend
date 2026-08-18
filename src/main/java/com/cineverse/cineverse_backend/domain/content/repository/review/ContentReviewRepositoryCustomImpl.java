package com.cineverse.cineverse_backend.domain.content.repository.review;

import com.cineverse.cineverse_backend.domain.content.dto.request.SearchContentReviewDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewListResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewResponseDTO;
import com.cineverse.cineverse_backend.domain.content.entity.QContent;
import com.cineverse.cineverse_backend.domain.content.entity.QContentRating;
import com.cineverse.cineverse_backend.domain.content.entity.QContentReview;
import com.cineverse.cineverse_backend.domain.image.entity.QImage;
import com.cineverse.cineverse_backend.domain.image.enums.ImageTargetType;
import com.cineverse.cineverse_backend.domain.image.enums.ImageType;
import com.cineverse.cineverse_backend.domain.user.entity.QUser;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContentReviewRepositoryCustomImpl implements ContentReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QContent content = QContent.content;
    private final QContentReview contentReview = QContentReview.contentReview;
    private final QContentRating contentRating = QContentRating.contentRating;
    private final QImage image = QImage.image;
    private final QUser user = QUser.user;

    @Override
    public ContentReviewResponseDTO findContentReviewByContentReviewId(Long userId, Long contentReviewId) {

        Expression<Boolean> writerExpression = (userId == null) ? ExpressionUtils.as(
                Expressions.constant(false),
                "writer"
        ) : new CaseBuilder()
                .when(contentReview.user.userId.eq(userId))
                .then(true)
                .otherwise(false)
                .as("writer");

        return queryFactory
                .select(Projections.fields(
                        ContentReviewResponseDTO.class,
                        contentReview.contentReviewId,
                        content.contentId,
                        content.title.as("contentTitle"),
                        content.releaseAt,
                        contentReview.reviewTitle,
                        contentReview.reviewText,
                        contentRating.score,
                        user.nickname,
                        user.email,
                        image.path.as("posterUrl"),
                        contentReview.createdAt,
                        writerExpression,
                        contentReview.spoiler
                ))
                .from(content)
                .leftJoin(contentReview)
                .on(
                        contentReview.content.contentId.eq(content.contentId)
                )
                .leftJoin(contentRating)
                .on(
                        contentRating.contentReview.contentReviewId.eq(contentReview.contentReviewId)
                )
                .leftJoin(user)
                .on(
                        user.userId.eq(contentReview.user.userId)
                )
                .leftJoin(image)
                .on(
                        image.targetId.eq(content.contentId),
                        image.targetType.eq(ImageTargetType.CONTENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )
                .where(
                        contentReview.contentReviewId.eq(contentReviewId),
                        contentReview.deleted.eq(false)
                )
                .fetchOne();
    }

    @Transactional
    @Override
    public Slice<ContentReviewListResponseDTO> findContentReviews(SearchContentReviewDTO searchContentReviewDTO, Pageable pageable) {

        List<ContentReviewListResponseDTO> contentReviewList = queryFactory
                .select(Projections.fields(
                        ContentReviewListResponseDTO.class,
                        contentReview.contentReviewId,
                        content.contentId,
                        content.title.as("contentTitle"),
                        content.releaseAt,
                        contentReview.reviewTitle,
                        contentReview.reviewText,
                        contentRating.score,
                        user.nickname,
                        user.email,
                        image.path.as("posterUrl"),
                        contentReview.spoiler,
                        contentReview.createdAt
                ))
                .from(content)
                .leftJoin(contentReview)
                .on(
                        contentReview.content.contentId.eq(content.contentId)
                )
                .leftJoin(contentRating)
                .on(
                        contentRating.contentReview.contentReviewId.eq(contentReview.contentReviewId)
                )
                .leftJoin(user)
                .on(
                        user.userId.eq(contentReview.user.userId)
                )
                .leftJoin(image)
                .on(
                        image.targetId.eq(content.contentId),
                        image.targetType.eq(ImageTargetType.CONTENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )
                .where(
                        contentReview.deleted.eq(false),
                        keywordContains(searchContentReviewDTO.getKeyword())
                )
                .orderBy(
                        contentReview.createdAt.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() +1L)
                .fetch();

        // 다음 값 존재 여부
        boolean hasNext = contentReviewList.size() > pageable.getPageSize();

        // 다음 값 존재시 contentReviewList = 21개에서 마지막 제외 20개 반환
        if (hasNext) {
            contentReviewList = contentReviewList.subList(0, pageable.getPageSize());
        }

        return new SliceImpl<>(contentReviewList, pageable, hasNext);
    }

    @Override
    public List<ContentReviewListResponseDTO> findContentReviewByUserId(Long userId) {
        return queryFactory
                .select(Projections.fields(
                        ContentReviewListResponseDTO.class,
                        contentReview.contentReviewId,
                        content.contentId,
                        content.title.as("contentTitle"),
                        content.releaseAt,
                        contentReview.reviewTitle,
                        contentReview.reviewText,
                        contentRating.score,
                        user.nickname,
                        user.email,
                        image.path.as("posterUrl"),
                        contentReview.spoiler,
                        contentReview.createdAt
                ))
                .from(contentReview)

                // 리뷰의 콘텐츠 및 작성자 조인
                .join(contentReview.content, content)
                .join(contentReview.user, user)

                .leftJoin(contentRating)
                .on(contentRating.contentReview.eq(contentReview))

                .leftJoin(image)
                .on(
                        image.targetId.eq(content.contentId),
                        image.targetType.eq(ImageTargetType.CONTENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )

                .where(
                        user.userId.eq(userId),
                        contentReview.deleted.isFalse()
                )

                // 최신순으로 최대 3개
                .orderBy(
                        contentReview.createdAt.desc(),
                        contentReview.contentReviewId.desc()
                )
                .limit(3)
                .fetch();
    }

    @Transactional
    @Override
    public Page<ContentReviewListResponseDTO> findUserContentReviews(Long userId, Pageable pageable) {

        List<ContentReviewListResponseDTO> contentReviewList = queryFactory
                .select(Projections.fields(
                        ContentReviewListResponseDTO.class,
                        contentReview.contentReviewId,
                        content.contentId,
                        content.title.as("contentTitle"),
                        content.releaseAt,
                        contentReview.reviewTitle,
                        contentReview.reviewText,
                        contentRating.score,
                        user.nickname,
                        user.email,
                        image.path.as("posterUrl"),
                        contentReview.spoiler,
                        contentReview.createdAt
                ))
                .from(content)
                .leftJoin(contentReview)
                .on(
                        contentReview.content.contentId.eq(content.contentId)
                )
                .leftJoin(contentRating)
                .on(
                        contentRating.contentReview.contentReviewId.eq(contentReview.contentReviewId)
                )
                .leftJoin(user)
                .on(
                        user.userId.eq(contentReview.user.userId)
                )
                .leftJoin(image)
                .on(
                        image.targetId.eq(content.contentId),
                        image.targetType.eq(ImageTargetType.CONTENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )
                .where(
                        contentReview.deleted.eq(false),
                        contentReview.user.userId.eq(userId)
                )
                .orderBy(
                        contentReview.createdAt.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(contentReview.count())
                .from(contentReview)
                .where(
                        contentReview.deleted.eq(false),
                        contentReview.user.userId.eq(userId)
                )
                .fetchOne();

        return new PageImpl<>(contentReviewList, pageable, Optional.ofNullable(total).orElse(0L));
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return content.title.containsIgnoreCase(keyword.trim());
    }
}
