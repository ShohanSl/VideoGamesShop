package com.example.videogamesshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    @GetMapping({
            "/catalog",
            "/games/**",
            "/developers/**",
            "/publishers/**",
            "/users/**",
            "/management/**"
    })
    public String forwardSpaRoutes() {
        return "forward:/index.html";
    }
}