package com.example.demo;

import com.example.demo.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {

	private final MovieService movieService;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public ApplicationRunner init() {
		return args -> {
			try {
				log.info("Loading movie data...");
				movieService.loadJson();
				log.info("Movie data loaded successfully");
			} catch (Exception e) {
				log.error("Failed to load movie data: {}", e.getMessage(), e);
				// Don't fail the application startup
			}
		};
	}
}