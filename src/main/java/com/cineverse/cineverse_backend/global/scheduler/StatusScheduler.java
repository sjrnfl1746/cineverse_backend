package com.cineverse.cineverse_backend.global.scheduler;

import com.cineverse.cineverse_backend.domain.content.service.content.ContentService;
import com.cineverse.cineverse_backend.domain.event.service.EventService;
import com.cineverse.cineverse_backend.domain.subscription.service.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class StatusScheduler {

    private final ContentService contentService;
    private final SubscriptionService subscriptionService;
    private final EventService eventService;

    @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
    public void updateStatus() {
        log.info("일일 콘텐츠 상태 변경 스케줄러 시작...");

        try {
            int contentCnt = updateContentStatus();
            int subscriptionCnt = updateSubscriptionStatus();
            int eventCnt = updateEventStatus();

            log.info("일일 상태 변경 완료: 콘텐츠={}, 구독={}건, 이벤트={}건", contentCnt,subscriptionCnt,eventCnt);
        } catch (Exception e) {
            log.error("일일 콘텐츠 상태 변경 스케줄러 실행 실패...", e);
        }
    }

    private int updateContentStatus() {
        try {
            return contentService.updateContentStatuses();
        } catch (Exception e) {
            log.error("콘텐츠 상태 변경 실패", e);
            return 0;
        }
    }

    private int updateSubscriptionStatus() {
        try {
            return subscriptionService.updateSubscriptionStatuses();
        } catch (Exception e) {
            log.error("구독 상태 변경 실패", e);
            return 0;
        }
    }

    private int updateEventStatus() {
        try {
            return eventService.updateEventStatuses();
        } catch (Exception e) {
            log.error("이벤트 상태 변경 실패", e);
            return 0;
        }
    }
}
