package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Slf4j
public class RenderKeepAliveController {

    private static final String MY_APP_URL = "https://megatestkr.onrender.com/health-check";

    private final RestTemplate restTemplate = new RestTemplate();
    private final AtomicInteger pingCount = new AtomicInteger(0);

    @GetMapping("/health-check")
    public String healthCheck() {
        return "UP";
    }

    @GetMapping("/manual-ping")
    public String manualPing() {
        sendPing("Manual");
        return "Manual Ping Sent!";
    }

    @Scheduled(fixedRate = 720000)
    public void scheduledKeepAlive() {
        LocalTime now = LocalTime.now();
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(18, 0);

        if (now.isAfter(start) && now.isBefore(end)) {
            sendPing("Auto");
        }
    }

    private void sendPing(String type) {
        try {
            restTemplate.getForObject(MY_APP_URL, String.class);
            int currentCount = pingCount.incrementAndGet();
            log.info("[{}] \ud551 \uc804\uc1a1 \uc644\ub8cc! (\ub204\uc801: {})", type, currentCount);
        } catch (Exception e) {
            log.error("[{}] \ud551 \uc2e4\ud328: {}", type, e.getMessage());
        }
    }
}
