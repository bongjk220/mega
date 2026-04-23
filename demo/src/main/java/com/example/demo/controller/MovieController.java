package com.example.demo.controller;

import com.example.demo.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService service;

    @GetMapping("/")
    public String home(Model model) {
        try {
            model.addAttribute("movies", service.getAll());
        } catch (Exception e) {
            model.addAttribute("movies", Collections.emptyList());
            log.error("Database connection failed", e);
        }
        return "index";
    }
}
