package com.cineverse.cineverse_backend.domain.terms.service;

import com.cineverse.cineverse_backend.domain.terms.dto.response.TermsResponseDTO;
import com.cineverse.cineverse_backend.domain.terms.entity.Terms;
import com.cineverse.cineverse_backend.domain.terms.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {

    private final TermsRepository termsRepository;

    @Override
    public List<TermsResponseDTO> getActiveTerms() {

        List<TermsResponseDTO> termsResponseDTOList = new ArrayList<>();

        List<Terms> termsList = termsRepository.findByActiveTrueOrderBySortOrderAsc();

        for (Terms terms : termsList) {
            TermsResponseDTO termsResponseDTO = TermsResponseDTO.builder()
                    .termsId(terms.getTermsId())
                    .title(terms.getTitle())
                    .content(terms.getContent())
                    .type(terms.getType().name())
                    .required(terms.getRequired())
                    .version(terms.getVersion())
                    .active(terms.getActive())
                    .sortOrder(terms.getSortOrder())
                    .createdAt(terms.getCreatedAt())
                    .updatedAt(terms.getUpdatedAt())
                    .build();

            termsResponseDTOList.add(termsResponseDTO);
        }

        return termsResponseDTOList;
    }
}
