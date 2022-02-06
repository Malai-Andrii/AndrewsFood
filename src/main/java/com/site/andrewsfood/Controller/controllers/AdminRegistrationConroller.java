package com.site.andrewsfood.Controller.controllers;

import com.site.andrewsfood.Controller.Utilities.ControllerUtils;
import com.site.andrewsfood.Model.domain.Miscellaneous;
import com.site.andrewsfood.Model.domain.enums.Role;
import com.site.andrewsfood.Model.domain.User;
import com.site.andrewsfood.Dao.MiscellaneousRepo;
import com.site.andrewsfood.Dao.UserRepo;
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
    private final UserRepo userRepo;
    private final MiscellaneousRepo miscellaneousRepo;

    public AdminRegistrationConroller(UserRepo userRepo, MiscellaneousRepo miscellaneousRepo) {
        this.userRepo = userRepo;
        this.miscellaneousRepo = miscellaneousRepo;
    }

    @GetMapping
    public String regAdminGet(Model model) {
        return "tempNoAuthorization/regAdmin";
    }

    @PostMapping
    public String registrateAdmin(@Valid User user, BindingResult bindingResult,
                                  @RequestParam String controlSequence, Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "tempNoAuthorization/regAdmin";
        }

        if (userRepo.findByUsername(user.getUsername()) != null) {
            model.addAttribute("messageUserExists", "Користувач з даним ім'ям вже існує!");
            return "tempNoAuthorization/regAdmin";
        }

        Miscellaneous miscellaneous = miscellaneousRepo.findById(1);
        if (!controlSequence.equals(miscellaneous.getControlSequence())) {
            model.addAttribute("secretError", "Контрольну послідовність введено неправильно!");
            return "tempNoAuthorization/regAdmin";
        }

        user.setActive(true);
        user.setRole(Collections.singleton(Role.ADMIN));
        userRepo.save(user);

        return "redirect:/login";
    }
}
