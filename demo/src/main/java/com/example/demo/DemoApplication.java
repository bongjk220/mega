package com.example.demo;

import com.example.demo.service.CrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public ApplicationRunner init(CrawlerService crawlerService) {
        return args -> {
            try {
                log.info("=== \uc560\ud50c\ub9ac\ucf00\uc774\uc158 \uc2dc\uc791 \uc2dc \ud06c\ub864\ub7ec \uc2e4\ud589 ===");
                crawlerService.runCrawler();
                log.info("=== \ud06c\ub864\ub7ec \uc2e4\ud589 \uc644\ub8cc ===");
            } catch (Exception e) {
                log.error("Failed to run crawler on startup: {}", e.getMessage(), e);
            }
        };
    }
}
