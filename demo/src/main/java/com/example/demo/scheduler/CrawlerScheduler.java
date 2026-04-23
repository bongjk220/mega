package com.example.demo.scheduler;

import com.example.demo.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrawlerScheduler {

    private final MovieService movieService;

    // 매일 아침 8시에 실행 (한국 시간 기준)
    @Scheduled(cron = "0 30 9 * * *", zone = "Asia/Seoul")
    public void runDailyCrawler() {
        log.info("=== 매일 아침 크롤러 실행 시작 ===");
        try {
            movieService.loadJson();
            log.info("=== 크롤러 실행 성공 ===");
        } catch (Exception e) {
            log.error("=== 크롤러 실행 실패 ===", e);
        }
    }

    // 테스트용: 매분 실행 (개발 중에만 사용)
    // @Scheduled(fixedRate = 60000)
    // public void runTestCrawler() {
    //     log.info("테스트 크롤러 실행");
    //     try {
    //         movieService.loadJson();
    //     } catch (Exception e) {
    //         log.error("테스트 크롤러 실패", e);
    //     }
    // }
}
