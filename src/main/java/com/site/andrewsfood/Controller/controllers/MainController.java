package com.site.andrewsfood.Controller.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String greeting(Model model) {
        return "tempNoAuthorization/greeting";
}

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/mainUser")
    public String mainUserGet(Model model) {
        return "tempUser/mainUser";
    }


}