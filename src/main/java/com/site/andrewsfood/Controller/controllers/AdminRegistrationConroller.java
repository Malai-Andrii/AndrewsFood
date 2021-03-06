package com.site.andrewsfood.Controller.controllers;

import com.site.andrewsfood.Controller.Utilities.ControllerUtils;
import com.site.andrewsfood.Model.domain.Miscellaneous;
import com.site.andrewsfood.Model.domain.Role;
import com.site.andrewsfood.Model.domain.User;
import com.site.andrewsfood.Dao.MiscellaneousRepo;
import com.site.andrewsfood.Dao.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/adminreg")
public class AdminRegistrationConroller {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MiscellaneousRepo miscellaneousRepo;


    @GetMapping
    public String regAdminGet(Model model) {
        return "tempNoAuthorization/regAdmin";
    }

    @PostMapping
    public String registrateAdmin(@Valid User user, BindingResult bindingResult, @RequestParam String controlSequence, Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "tempNoAuthorization/regAdmin";
        }

        User userFromDB = userRepo.findByUsername(user.getUsername());
        if (userFromDB != null) {
            model.addAttribute("messageUserExists", "Користувач з даним ім'ям вже існує!");
            return "tempNoAuthorization/regAdmin";
        }

        Miscellaneous miscellaneous = miscellaneousRepo.findById(1);
        String secret = miscellaneous.getControlSequence();

        if (!controlSequence.equals(secret)) {
            model.addAttribute("secretError", "Контрольну послідовність введено неправильно!");
            return "tempNoAuthorization/regAdmin";
        }

        user.setActive(true);
        user.setRole(Collections.singleton(Role.ADMIN));
        userRepo.save(user);

        return "redirect:/login";
    }
}
