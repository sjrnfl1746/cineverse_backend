package com.cineverse.cineverse_backend.domain.content.repository.content;

import com.cineverse.cineverse_backend.domain.content.dto.request.SearchRandomContentRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.request.SearchRequestDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentDetailResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentSliceListResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentTop5ResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ContentRepositoryCustom {

    Page<ContentResponseDTO> findAllByKeywordAndContentStatus(SearchRequestDTO searchRequestDTO, Pageable pageable);

    ContentDetailResponseDTO findContentDetailByContentId(Long contentId);

    List<ContentTop5ResponseDTO> findTop6Content();

    // 무작위 콘텐츠 20개 조회 - 무한 스크롤
    Slice<ContentSliceListResponseDTO> findRandomContents(SearchRandomContentRequestDTO searchRandomContentRequestDTO, Pageable pageable);

    // 공개중인 콘텐츠 조회
    ContentDetailResponseDTO findPublishedContentByContentId(Long contentId);
}
