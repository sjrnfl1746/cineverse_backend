package com.cineverse.cineverse_backend.domain.content.service.content;

import com.cineverse.cineverse_backend.domain.content.dto.request.ContentRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.SearchRandomContentRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.SearchRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.*;
import com.cineverse.cineverse_backend.domain.content.entity.Content;
import com.cineverse.cineverse_backend.domain.content.entity.ContentGenre;
import com.cineverse.cineverse_backend.domain.content.entity.Genre;
import com.cineverse.cineverse_backend.domain.content.enums.ContentStatus;
import com.cineverse.cineverse_backend.domain.content.repository.ContentGenreRepository;
import com.cineverse.cineverse_backend.domain.content.repository.content.ContentRepository;
import com.cineverse.cineverse_backend.domain.content.repository.genre.GenreRepository;
import com.cineverse.cineverse_backend.domain.content.repository.userWishlist.UserWishlistRepository;
import com.cineverse.cineverse_backend.domain.dashboard.dto.SummaryDTO;
import com.cineverse.cineverse_backend.domain.image.enums.ImageTargetType;
import com.cineverse.cineverse_backend.domain.image.enums.ImageType;
import com.cineverse.cineverse_backend.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final GenreRepository genreRepository;
    private final ContentGenreRepository contentGenreRepository;
    private final UserWishlistRepository userWishlistRepository;
    private final ImageService imageService;

    @Transactional
    @Override
    public Long addContent(ContentRequestDTO contentRequestDTO, MultipartFile file) {

        // 콘텐츠 저장
        Content content = Content.builder()
                .title(contentRequestDTO.getTitle())
                .ogTitle(contentRequestDTO.getOgTitle())
                .description(contentRequestDTO.getDescription())
                .releaseAt(contentRequestDTO.getReleaseAt())
                .endAt(contentRequestDTO.getEndAt())
                .runningTime(contentRequestDTO.getRunningTime())
                .productionCountry(contentRequestDTO.getProductionCountry())
                .ageRating(contentRequestDTO.getAgeRating())
                .contentStatus(contentRequestDTO.getContentStatus())
                .trailerUrl(contentRequestDTO.getTrailerUrl())
                .build();

        contentRepository.save(content);

        // 장르 저장
        List<Long> genreIds = contentRequestDTO.getGenreIds();

        if (genreIds == null || genreIds.isEmpty()) {
            throw new RuntimeException("장르 id가 존재하지 않습니다.");
        }

        for (Long genreId : genreIds) {
            // 장르 조회 후 저장
            Genre genre = genreRepository.findByGenreId(genreId);

            ContentGenre contentGenre = ContentGenre.builder()
                    .content(content)
                    .genre(genre)
                    .build();
            contentGenreRepository.save(contentGenre);
        }

        // 이미지 저장
        imageService.saveImage(file, ImageTargetType.CONTENT, content.getContentId(), ImageType.POSTER, 0, true);

        return content.getContentId();
    }

    @Transactional
    @Override
    public ContentDetailResponseDTO getContentByContentId(Long contentId) {

        // 콘텐츠 조회
        ContentDetailResponseDTO contentDetailResponseDTO = contentRepository.findContentDetailByContentId(contentId);

        if (contentDetailResponseDTO == null) {
            throw new RuntimeException("콘텐츠가 존재하지 않습니다.");
        }

        // 해당하는 장르들 조회
        List<GenreResponseDTO> genres = genreRepository.findGenresByContentId(contentId);
        contentDetailResponseDTO.setGenres(genres);

        return contentDetailResponseDTO;
    }

    @Transactional
    @Override
    public Page<ContentResponseDTO> getAllContent(SearchRequestDTO searchRequestDTO, Pageable pageable) {

        return contentRepository.findAllByKeywordAndContentStatus(searchRequestDTO, pageable);
    }

    @Transactional
    @Override
    public void updateContent(ContentRequestDTO contentRequestDTO, MultipartFile file, Long contentId) {

        // 콘텐츠 조회
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("콘텐츠가 존재하지 않습니다."));

        // 콘텐츠 장르 조회
        List<Long> genreIds = contentRequestDTO.getGenreIds();

        if (genreIds == null || genreIds.isEmpty()) {
            throw new RuntimeException("장르 id가 존재하지 않습니다.");
        }

        List<Long> distinctGenreIds = genreIds.stream().distinct().toList();

        List<Genre> genres = genreRepository.findAllById(distinctGenreIds);

        if (genres.size() != distinctGenreIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 장르가 포함되어 있습니다.");
        }

        // 콘텐츠 수정
        content.update(contentRequestDTO.getTitle(), contentRequestDTO.getOgTitle(), contentRequestDTO.getDescription(),
                contentRequestDTO.getReleaseAt(), contentRequestDTO.getEndAt(), contentRequestDTO.getRunningTime(),
                contentRequestDTO.getProductionCountry(), contentRequestDTO.getAgeRating(),
                contentRequestDTO.getContentStatus(), contentRequestDTO.getTrailerUrl());

        // 기존 장르 삭제
        contentGenreRepository.deleteAllByContentId(contentId);

        List<ContentGenre> contentGenres = genres.stream()
                .map(genre -> ContentGenre.builder()
                        .content(content)
                        .genre(genre)
                        .build())
                .toList();

        contentGenreRepository.saveAll(contentGenres);

        // 포스터 재등록 시
        if (file != null && !file.isEmpty()) {
            imageService.updateImage(file, ImageTargetType.CONTENT, contentId, ImageType.POSTER, 0, true);
        }
    }

    @Transactional
    @Override
    public void deleteContent(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("콘텐츠가 존재하지 않습니다."));

        content.delete();
    }

    @Transactional
    @Override
    public SummaryDTO getAllContentCount() {

        return SummaryDTO.builder()
                .title("전체 콘텐츠")
                .value(contentRepository.countByDeletedFalse())
                .money(false)
                .build();
    }

    @Transactional
    @Override
    public List<ContentDashboardResponseDTO> getAllContentDashboard() {

        return contentRepository.findTop4ByDeletedFalseOrderByCreatedAtDesc()
                .stream().map(content -> ContentDashboardResponseDTO.builder()
                        .contentId(content.getContentId())
                        .title(content.getTitle())
                        .contentStatus(content.getContentStatus())
                        .createdAt(content.getCreatedAt())
                        .build())
                .toList();
    }

    @Transactional
    @Override
    public List<ContentTopViewResponseDTO> getAllContentTopView() {
        return contentRepository.findTop5ByDeletedFalseOrderByViewCntDescCreatedAtDesc()
                .stream().map(content -> ContentTopViewResponseDTO.builder()
                        .contentId(content.getContentId())
                        .title(content.getTitle())
                        .viewCnt(content.getViewCnt())
                        .build())
                .toList();
    }

    @Transactional
    @Override
    public List<ContentStatusResponseDTO> getAllContentStatus() {

        /***** 삭제되지 않은 전체 콘텐츠 개수 *****/
        Long totalCont = contentRepository.countByDeletedFalse();

        List<ContentStatusResponseDTO> contentStatusResponseDTOList = new ArrayList<>();

        /***** 공개 중인 콘텐츠 정보 *****/
        Long publishedContentCnt = contentRepository.countByContentStatusAndDeletedFalse(ContentStatus.PUBLISHED);

        ContentStatusResponseDTO publishedContent = ContentStatusResponseDTO.builder()
                .contentStatus(ContentStatus.PUBLISHED)
                .value(publishedContentCnt)
                .percent(getPercent(publishedContentCnt, totalCont))
                .build();
        contentStatusResponseDTOList.add(publishedContent);

        /***** 공개 예정인 콘텐츠 정보 *****/
        Long readyContentCnt = contentRepository.countByContentStatusAndDeletedFalse(ContentStatus.READY);

        ContentStatusResponseDTO readyContent = ContentStatusResponseDTO.builder()
                .contentStatus(ContentStatus.READY)
                .value(readyContentCnt)
                .percent(getPercent(readyContentCnt, totalCont))
                .build();
        contentStatusResponseDTOList.add(readyContent);

        /***** 종료된 콘텐츠 정보 *****/
        Long endContentCnt = contentRepository.countByContentStatusAndDeletedFalse(ContentStatus.END);

        ContentStatusResponseDTO endContent = ContentStatusResponseDTO.builder()
                .contentStatus(ContentStatus.END)
                .value(endContentCnt)
                .percent(getPercent(endContentCnt, totalCont))
                .build();
        contentStatusResponseDTOList.add(endContent);

        /***** 숨김 처리된 콘텐츠 정보 *****/
        Long hiddenContentCnt = contentRepository.countByContentStatusAndDeletedFalse(ContentStatus.HIDDEN);

        ContentStatusResponseDTO hiddenContent = ContentStatusResponseDTO.builder()
                .contentStatus(ContentStatus.HIDDEN)
                .value(hiddenContentCnt)
                .percent(getPercent(hiddenContentCnt, totalCont))
                .build();
        contentStatusResponseDTOList.add(hiddenContent);

        return contentStatusResponseDTOList;
    }

    @Transactional
    @Override
    public List<ContentTop5ResponseDTO> getTop6Content() {

        // 콘텐츠 조회
        List<ContentTop5ResponseDTO> contentList = contentRepository.findTop6Content();

        for (ContentTop5ResponseDTO content : contentList) {
            // 장르 조회
            List<GenreResponseDTO> genreList = genreRepository.findGenresByContentId(content.getContentId());
            content.setGenres(genreList);
        }

        return contentList;
    }

    @Transactional
    @Override
    public Slice<ContentSliceListResponseDTO> getRandomContentSlice(SearchRandomContentRequestDTO searchRandomContentRequestDTO, Pageable pageable) {

        Slice<ContentSliceListResponseDTO> contentSlice = contentRepository.findRandomContents(searchRandomContentRequestDTO, pageable);

        for (ContentSliceListResponseDTO content : contentSlice) {
            // 각 각 해당하는 장르들 조회
            List<GenreResponseDTO> genres = genreRepository.findGenresByContentId(content.getContentId());
            content.setGenres(genres);
        }

        return contentSlice;
    }

    @Transactional
    @Override
    public ContentDetailResponseDTO getPublishedContentById(Long userId, Long contentId) {

        ContentDetailResponseDTO contentDetailResponseDTO = contentRepository.findPublishedContentByContentId(contentId);

        if (contentDetailResponseDTO == null) {
            throw new RuntimeException("콘텐츠가 존재하지 않습니다.");
        }

        // 장르 조회
        List<GenreResponseDTO> genres = genreRepository.findGenresByContentId(contentId);
        contentDetailResponseDTO.setGenres(genres);

        // 찜 여부 확인
        boolean isWishlisted = userWishlistRepository.existsByUser_UserIdAndContent_ContentId(userId, contentId);
        contentDetailResponseDTO.setWishlisted(isWishlisted);

        // 조회수 + 1
        Content content = contentRepository.findById(contentId).orElseThrow(
                () -> new RuntimeException("콘텐츠가 존재하지 않습니다."));
        content.updateViewCnt();

        return contentDetailResponseDTO;
    }

    @Transactional
    @Override
    public int updateContentStatuses() {

        // 한국 서울 시간 기준
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        int endedCnt = contentRepository.endContents(today, List.of(ContentStatus.READY, ContentStatus.PUBLISHED), ContentStatus.END);
        int publishedCnt = contentRepository.publishedContents(today, ContentStatus.READY, ContentStatus.PUBLISHED);

        return endedCnt + publishedCnt;
    }

    // 퍼센트 반환
    private Double getPercent(Long value, Long totalCnt) {

        if (totalCnt == 0) {
            return 0.0;
        }

        return (value * 100.0) / totalCnt;
    }

}
