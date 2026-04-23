package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class CrawlerService {

    public void runCrawler() {
        try {
            log.info("\uba54\uac00\ubc15\uc2a4 \ud06c\ub864\ub7ec \uc2dc\uc791...");

            File projectDir = new File(System.getProperty("user.dir"));
            File venvPython = new File(projectDir, ".venv\\Scripts\\python.exe");
            List<String> command = venvPython.exists()
                    ? List.of(venvPython.getAbsolutePath(), "crawler/crawler.py")
                    : List.of("python", "crawler/crawler.py");

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(projectDir);
            processBuilder.redirectErrorStream(true);
            processBuilder.environment().put("PYTHONIOENCODING", "UTF-8");
            processBuilder.environment().put("PYTHONUTF8", "1");

            log.info("Crawler command: {}", command.get(0));

            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("Crawler: {}", line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("\ud06c\ub864\ub7ec \uc2e4\ud589 \uc131\uacf5");
            } else {
                log.error("\ud06c\ub864\ub7ec \uc2e4\ud589 \uc2e4\ud328 (exit code: {})", exitCode);
            }
        } catch (Exception e) {
            log.error("\ud06c\ub864\ub7ec \uc2e4\ud589 \uc911 \uc624\ub958 \ubc1c\uc0dd: {}", e.getMessage(), e);
        }
    }
}
