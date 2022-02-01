package com.site.andrewsfood.Controller.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginGet(Model model) {
        return "tempNoAuthorization/login";
    }
}
