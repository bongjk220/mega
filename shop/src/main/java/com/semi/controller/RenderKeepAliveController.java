package com.semi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Slf4j
public class RenderKeepAliveController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AtomicInteger pingCount = new AtomicInteger(0);

    @Value("${app.url:https://dadream.onrender.com}")
    private String myAppUrl;

    // 헬스체크 엔드포인트
    @GetMapping("/health-check")
    public String healthCheck() {
        return "UP";
    }

    // 14분마다 자가 핑 (09:00 ~ 18:00 KST)
    @Scheduled(fixedRate = 840000) // 14분마다 실행 (840,000ms = 14분)
    public void scheduledKeepAlive() {
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(18, 0);

        if (now.isAfter(start) && now.isBefore(end)) {
            try {
                restTemplate.getForObject(myAppUrl + "/health-check", String.class);
                log.info("[Auto] 핑 전송 완료! (누적: {})", pingCount.incrementAndGet());
            } catch (Exception e) {
                log.warn("[Auto] 핑 전송 실패: {}", e.getMessage());
            }
        }
    }
}