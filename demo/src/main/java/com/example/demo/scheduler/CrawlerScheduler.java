package com.example.demo.scheduler;

import com.example.demo.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrawlerScheduler {

    private final CrawlerService crawlerService;

    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void runDailyCrawler() {
        log.info("=== \ub9e4\uc77c \uc544\uce68 \ud06c\ub864\ub7ec \uc2e4\ud589 \uc2dc\uc791 ===");
        try {
            crawlerService.runCrawler();
            log.info("=== \ud06c\ub864\ub7ec \uc2e4\ud589 \uc131\uacf5 ===");
        } catch (Exception e) {
            log.error("=== \ud06c\ub864\ub7ec \uc2e4\ud589 \uc2e4\ud328 ===", e);
        }
    }
}
