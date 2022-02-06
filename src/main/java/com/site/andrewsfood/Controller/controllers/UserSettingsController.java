package com.site.andrewsfood.Controller.controllers;

import com.site.andrewsfood.Controller.Utilities.ControllerUtils;
import com.site.andrewsfood.Model.domain.enums.Contradictions;
import com.site.andrewsfood.Model.domain.CustomUserDetails;
import com.site.andrewsfood.Model.domain.User;
import com.site.andrewsfood.Dao.UserRepo;
import com.site.andrewsfood.Service.MailService;
import com.site.andrewsfood.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserSettingsController {
    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/userSettings")
    public String userSettingsGet(String username, Model model) {
        if (username != null) {
            model.addAttribute("user", userService.findUserByUsername(username));
            model.addAttribute("customUserDetails", userService.findUserByUsername(username).getCustomUserDetails());
            Set<Contradictions> contras = userService.findUserByUsername(username).getCustomUserDetails().getContradictions();
            Set<String> contradictions = new HashSet<>();
            for (Contradictions c : contras) {
                contradictions.add(c.toString());
            }
            model.addAttribute("contradictions", contradictions);
        }
        return "userSettings";
    }


//    @PostMapping("/userSettings/{username}")
//    public String userSettingsEdit(@PathVariable("username") String username, Model model) {
//        return "userSettings";
//    }

    @PostMapping("/userSettings")
    public String userSettingsPost(@Valid User user, BindingResult bindingResult, @Valid CustomUserDetails customUserDetails,
                                   BindingResult bindingResultDetails, Model model, @RequestParam Map<String, String> form) {

        String returnStatement = "redirect:/userSettings?username=" + user.getUsername();
            if (bindingResultDetails.hasErrors() || bindingResult.hasErrors()) {
                if (bindingResult.hasErrors()) {
                    Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
                    model.mergeAttributes(errors);
                }
                if (bindingResultDetails.hasErrors()) {
                    Map<String, String> errors2 = ControllerUtils.getErrors(bindingResultDetails);
                    model.mergeAttributes(errors2);
                }
                return returnStatement;
            }

            CustomUserDetails userFromDB_email = userService.loadUserByEmail(customUserDetails.getEmail());

            if (userFromDB_email != null) {
                model.addAttribute("emailError", "Дана пошта вже використовується!");
                return returnStatement;
            }

            UserDetails userFromDB_username = userService.loadUserByUsername(user.getUsername());

            if (userFromDB_username != null) {
                model.addAttribute("usernameError", "Дане ім'я вже заняте!");
                return returnStatement;
            }

            //---------------------------------

            Set<Contradictions> contras = new HashSet<Contradictions>();
            customUserDetails.setContradictions(contras);

            Set<String> contradictions = Arrays.stream(Contradictions.values())
                    .map(Contradictions::name)
                    .collect(Collectors.toSet());

            for (String key : form.keySet()) {
                if (contradictions .contains(key)) {
                    System.out.println(Contradictions.valueOf(key));
                    customUserDetails.getContradictions().add(Contradictions.valueOf(key));
                }
            }

            //---------------------------------


            user.setActive(false);
            customUserDetails.setActivationCode(UUID.randomUUID().toString());
            if (customUserDetails.getNutritionStyle() != "sport") {
                customUserDetails.setBodyConstitution("default");
                customUserDetails.setTrainType("default");
            }
            user.setCustomUserDetails(customUserDetails);

            userRepo.save(user);

            return "mainUser";
    }
}
