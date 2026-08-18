package com.cineverse.cineverse_backend.domain.content.repository.content;

import com.cineverse.cineverse_backend.domain.content.dto.request.SearchRandomContentRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.SearchRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentDetailResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentSliceListResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentTop5ResponseDTO;
import com.cineverse.cineverse_backend.domain.content.entity.QContent;
import com.cineverse.cineverse_backend.domain.content.enums.ContentStatus;
import com.cineverse.cineverse_backend.domain.image.entity.QImage;
import com.cineverse.cineverse_backend.domain.image.enums.ImageTargetType;
import com.cineverse.cineverse_backend.domain.image.enums.ImageType;
import com.cineverse.cineverse_backend.domain.video.entity.QVideo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContentRepositoryCustomImpl implements ContentRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QContent content = QContent.content;
    private final QImage image = QImage.image;
    private final QVideo video = QVideo.video;

    @Override
    public Page<ContentResponseDTO> findAllByKeywordAndContentStatus(SearchRequestDTO searchRequestDTO, Pageable pageable) {

        List<ContentResponseDTO> contentList = queryFactory
                .select(Projections.constructor(
                        ContentResponseDTO.class,
                        content.contentId,
                        content.title,
                        content.ogTitle,
                        content.runningTime,
                        content.productionCountry,
                        content.ageRating,
                        content.contentStatus,
                        image.path
                ))
                .from(content)
                .leftJoin(image)
                .on(
                        image.targetId.eq(content.contentId),
                        image.targetType.eq(ImageTargetType.CONTENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )
                .where(
                        keywordContains(searchRequestDTO.getKeyword()),
                        contentStatusEq(searchRequestDTO.getContentStatus()),
                        content.deleted.eq(false)
                )
                .orderBy(content.createdAt.desc(), content.contentId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(content.count())
                .from(content)
                .where(
                        keywordContains(searchRequestDTO.getKeyword()),
                        contentStatusEq(searchRequestDTO.getContentStatus()),
                        content.deleted.eq(false)
                )
                .fetchOne();

        return new PageImpl<>(
                contentList,
                pageable,
                Optional.ofNullable(total).orElse(0L)
        );

    }

    @Override
    public ContentDetailResponseDTO findContentDetailByContentId(Long contentId) {

        return queryFactory
                .select(Projections.fields(
                        ContentDetailResponseDTO.class,
                        content.contentId,
                        content.title,
                        content.ogTitle,
                        content.description,
                        content.releaseAt,
                        content.endAt,
                        content.runningTime,
                        content.productionCountry,
                        content.ageRating,
                        content.contentStatus,
                        content.trailerUrl,
                        image.path.as("posterUrl"),
                        video.path.as("videoUrl")
                ))
                .from(content)
                .leftJoin(image)
                .on(
                        image.targetId.eq(content.contentId),
                        image.targetType.eq(ImageTargetType.CONTENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )
                .leftJoin(video)
                .on(
                        video.content.contentId.eq(content.contentId),
                        video.active.eq(true),
                        video.deleted.eq(false)
                )
                .where(
                        content.contentId.eq(contentId),
                        content.deleted.eq(false)
                )
                .fetchOne();
    }

    @Override
    public List<ContentTop5ResponseDTO> findTop6Content() {
        return queryFactory.select(Projections.fields(
                        ContentTop5ResponseDTO.class,
                        content.contentId,
                        content.title,
                        image.path.as("thumbnailUrl")
                ))
                .from(content)
                .leftJoin(image)
                .on(
                        image.targetId.eq(content.contentId),
                        image.targetType.eq(ImageTargetType.CONTENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )
                .where(
                        content.deleted.eq(false)
                )
                .orderBy(content.viewCnt.desc())
                .limit(6)
                .fetch();
    }

    @Transactional
    @Override
    public Slice<ContentSliceListResponseDTO> findRandomContents(SearchRandomContentRequestDTO searchRandomContentRequestDTO, Pageable pageable) {

        // ORDER BY RAND(seed값) 함수 생성
        NumberExpression<Double> randomOrder = Expressions.numberTemplate(
                Double.class,
                "function('rand', {0})",
                searchRandomContentRequestDTO.getSeed()
        );

        List<ContentSliceListResponseDTO> contentList = queryFactory
                .select(Projections.fields(
                        ContentSliceListResponseDTO.class,
                        content.contentId,
                        content.title,
                        image.path.as("thumbnailUrl")
                ))
                .from(content)
                .leftJoin(image)
                .on(
                        image.targetId.eq(content.contentId),
                        image.targetType.eq(ImageTargetType.CONTENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )
                .where(
                        content.contentStatus.eq(ContentStatus.PUBLISHED),
                        content.deleted.eq(false),
                        keywordContains(searchRandomContentRequestDTO.getKeyword())
                )
                .orderBy(
                        randomOrder.asc(),
                        content.contentId.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1L) // 21개 조회
                .fetch();

        // 다음 값 존재 여부
        boolean hasNext = contentList.size() > pageable.getPageSize();

        // 다음 값이 존재한다면 contentList = 21개에서 마지막걸 제외한 20개 반환
        if (hasNext) {
            contentList = contentList.subList(0, pageable.getPageSize());
        }

        return new SliceImpl<>(contentList, pageable, hasNext);
    }

    @Override
    public ContentDetailResponseDTO findPublishedContentByContentId(Long contentId) {
        return queryFactory
                .select(Projections.fields(
                        ContentDetailResponseDTO.class,
                        content.contentId,
                        content.title,
                        content.ogTitle,
                        content.description,
                        content.releaseAt,
                        content.endAt,
                        content.runningTime,
                        content.productionCountry,
                        content.ageRating,
                        content.contentStatus,
                        image.path.as("posterUrl")
                ))
                .from(content)
                .leftJoin(image)
                .on(
                        image.targetId.eq(content.contentId),
                        image.targetType.eq(ImageTargetType.CONTENT),
                        image.imageType.eq(ImageType.POSTER),
                        image.primaryImage.isTrue()
                )
                .where(
                        content.contentId.eq(contentId),
                        content.deleted.eq(false),
                        content.contentStatus.eq(ContentStatus.PUBLISHED)
                )
                .fetchOne();
    }

    private BooleanExpression keywordContains(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return content.title.containsIgnoreCase(keyword.trim());
    }

    private BooleanExpression contentStatusEq(ContentStatus contentStatus) {

        return contentStatus == null ? null : content.contentStatus.eq(contentStatus);
    }
}
