package com.example.videogamesshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    @GetMapping({
            "/user",
            "/user/**",
            "/admin",
            "/admin/**"
    })
    public String forwardSpaRoutes() {
        return "forward:/index.html";
    }
}
