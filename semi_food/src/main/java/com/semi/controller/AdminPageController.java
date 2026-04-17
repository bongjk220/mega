package com.semi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @GetMapping
    public String adminPage() {
        return "dadream-admin";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/orders")
    public String orders() {
        return "admin-orders";
    }

    @GetMapping("/products")
    public String products() {
        return "admin-products";
    }

    @GetMapping("/members")
    public String members() {
        return "admin-members";
    }
}
