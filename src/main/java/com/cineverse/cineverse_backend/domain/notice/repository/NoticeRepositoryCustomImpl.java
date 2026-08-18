package com.cineverse.cineverse_backend.domain.notice.repository;

import com.cineverse.cineverse_backend.domain.notice.dto.request.SearchNoticeDTO;
import com.cineverse.cineverse_backend.domain.notice.dto.response.NoticeResponseDTO;
import com.cineverse.cineverse_backend.domain.notice.entity.QNotice;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QNotice notice = QNotice.notice;

    @Transactional
    @Override
    public Page<NoticeResponseDTO> findAllNotice(Pageable pageable) {

        List<NoticeResponseDTO> noticeList = queryFactory
                .select(Projections.fields(
                        NoticeResponseDTO.class,
                        notice.noticeId,
                        notice.title,
                        notice.content,
                        notice.viewCnt,
                        notice.pinned,
                        notice.createdAt
                ))
                .from(notice)
                .where(
                        notice.deleted.eq(false)
                )
                .orderBy(
                        // pinned가 true인 것 부터 조회
                        notice.pinned.desc(),

                        // 최신거 부터
                        notice.createdAt.desc(),

                        // id를 기준으로 정렬
                        notice.noticeId.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(notice.count())
                .from(notice)
                .where(
                        notice.deleted.eq(false)
                )
                .fetchOne();

        return new PageImpl<>(noticeList, pageable, Optional.ofNullable(total).orElse(0L));
    }

    @Transactional
    @Override
    public Page<NoticeResponseDTO> findAllNoticeOrderByCreatedAtDesc(SearchNoticeDTO searchNoticeDTO, Pageable pageable) {
        List<NoticeResponseDTO> noticeList = queryFactory
                .select(Projections.fields(
                        NoticeResponseDTO.class,
                        notice.noticeId,
                        notice.title,
                        notice.content,
                        notice.viewCnt,
                        notice.pinned,
                        notice.createdAt
                ))
                .from(notice)
                .where(
                        notice.deleted.eq(false),
                        keywordContains(searchNoticeDTO.getKeyword())
                )
                .orderBy(
                        notice.createdAt.desc(),
                        notice.noticeId.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(notice.count())
                .from(notice)
                .where(
                        notice.deleted.eq(false)
                )
                .fetchOne();

        return new PageImpl<>(noticeList, pageable, Optional.ofNullable(total).orElse(0L));
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()){
            return null;
        }
        return notice.title.containsIgnoreCase(keyword);
    }
}
