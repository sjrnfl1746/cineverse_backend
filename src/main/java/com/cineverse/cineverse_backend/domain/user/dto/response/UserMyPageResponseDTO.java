package com.cineverse.cineverse_backend.domain.user.dto.response;

import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewListResponseDTO;
import com.cineverse.cineverse_backend.domain.content.dto.response.ContentReviewResponseDTO;
import com.cineverse.cineverse_backend.domain.subscription.dto.response.SubscriptionPlanResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMyPageResponseDTO {

    // 구독 정보
    SubscriptionPlanResponseDTO subscriptionPlan;

    // 내가 작성한 리뷰 / 찜한 콘텐츠
    List<UserSummaryResponseDTO> userSummaryList;

    // 최근 작성한 리뷰 - 3개
    List<ContentReviewListResponseDTO> contentReviews;
}
