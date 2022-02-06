package com.site.andrewsfood.Controller.controllers;

import com.site.andrewsfood.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mainAdmin")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserEditController {

    final UserService userService;

    public UserEditController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userEdit")
    public String userEditGet(Model model) {
//        model.addAttribute("commonContradition", userService.CommonContradition());
        return addAtributes(model);
    }

    @PostMapping("/userEdit")
    public String userEditPost(Model model) {
        return "tempAdmin/userEdit";
    }

    @PostMapping("/userDelete/{username}")
    public String deleteUserPost(@PathVariable("username") String username, Model model) {
        userService.deleteUserByName(username);
        return addAtributes(model);
    }

    private String addAtributes(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("allUsers", userService.allUsersCount());
        model.addAttribute("avgAge", userService.AverageAge());
        model.addAttribute("avgCalority", userService.AverageCalority());
        model.addAttribute("avgHeight", userService.AverageHeight());
        model.addAttribute("avgWeight", userService.AverageWeight());
        model.addAttribute("commonBodyConstitution", userService.CommonBodyConstitution());
        model.addAttribute("commonNutritionStyle", userService.CommonNutritionStyle());
        return "tempAdmin/userEdit";
    }
}
