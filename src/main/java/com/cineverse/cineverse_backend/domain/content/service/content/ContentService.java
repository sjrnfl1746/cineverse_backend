package com.cineverse.cineverse_backend.domain.content.service.content;

import com.cineverse.cineverse_backend.domain.content.dto.request.ContentRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.SearchRandomContentRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.SearchRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.*;
import com.cineverse.cineverse_backend.domain.dashboard.dto.SummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContentService {

    // 콘텐츠 등록
    Long addContent(ContentRequestDTO contentRequestDTO, MultipartFile file);

    // 콘텐츠 단건 조회
    ContentDetailResponseDTO getContentByContentId(Long contentId);

    // 콘텐츠 리스트 조회
    Page<ContentResponseDTO> getAllContent(SearchRequestDTO searchRequestDTO, Pageable pageable);

    // 콘텐츠 수정
    void updateContent(ContentRequestDTO contentRequestDTO, MultipartFile file, Long contentId);

    // 콘텐츠 삭제
    void deleteContent(Long contentId);

    // 전체 콘텐츠 요약 반환
    SummaryDTO getAllContentCount();

    // 최근 등록한 4개 콘텐츠 반환
    List<ContentDashboardResponseDTO> getAllContentDashboard();

    // top 5 시청 콘텐츠 반환
    List<ContentTopViewResponseDTO> getAllContentTopView();

    // 전체 콘텐츠 상태 반환
    List<ContentStatusResponseDTO> getAllContentStatus();

    // 콘텐츠 top5 영화 조회
    List<ContentTop5ResponseDTO> getTop6Content();

    // 랜덤 20개 함수 조회
    Slice<ContentSliceListResponseDTO> getRandomContentSlice(SearchRandomContentRequestDTO searchRandomContentRequestDTO, Pageable pageable);

    // 시청가능한 콘텐츠 단건 조회
    ContentDetailResponseDTO getPublishedContentById(Long userId, Long contentId);

    // 콘텐츠 상태 변경
    int updateContentStatuses();
}
