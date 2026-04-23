package com.example.demo.controller;

import com.example.demo.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService service;

    // 메인 페이지
    @GetMapping("/")
    public String home(Model model) {
        try {
            model.addAttribute("movies", service.getAll());
        } catch (Exception e) {
            // 데이터베이스 연결 실패 시 빈 리스트 전달
            model.addAttribute("movies", java.util.Collections.emptyList());
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return "index";
    }

    // JSON → DB 적재
    @GetMapping("/load")
    @ResponseBody
    public String load() throws Exception {
        service.loadJson();
        return "DB 적재 완료";
    }
}