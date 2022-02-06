package com.site.andrewsfood.Controller.controllers;

import com.site.andrewsfood.Controller.Utilities.ControllerUtils;
import com.site.andrewsfood.Model.domain.*;
import com.site.andrewsfood.Dao.UserRepo;
import com.site.andrewsfood.Model.domain.enums.BodyConstitution;
import com.site.andrewsfood.Model.domain.enums.Contradictions;
import com.site.andrewsfood.Model.domain.enums.NutritionStyle;
import com.site.andrewsfood.Model.domain.enums.TrainType;
import com.site.andrewsfood.Service.MailService;
import com.site.andrewsfood.Service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserRepo userRepo;
    private final UserService userService;
    private final MailService mailService;

    public RegistrationController(UserRepo userRepo, UserService userService, MailService mailService) {
       this.userRepo = userRepo;
       this.userService = userService;
       this.mailService = mailService;
    }

    @GetMapping
    public String registrationGet(Model model) {
        return "tempNoAuthorization/registration";
    }

    @PostMapping
    public String registrateUser(@Valid User user, BindingResult bindingResult, @Valid CustomUserDetails customUserDetails,
                                 BindingResult bindingResultDetails, Model model, @RequestParam Map<String, String> form) {

        if (bindingResultDetails.hasErrors() || bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
                model.mergeAttributes(errors);

            }
            if (bindingResultDetails.hasErrors()) {
                Map<String, String> errors2 = ControllerUtils.getErrors(bindingResultDetails);
                model.mergeAttributes(errors2);
            }
            return "tempNoAuthorization/registration";
        }

        CustomUserDetails userFromDB_email = userService.loadUserByEmail(customUserDetails.getEmail());

        if (userFromDB_email != null) {
            model.addAttribute("emailError", "Дана пошта вже використовується!");
            return "tempNoAuthorization/registration";
        }

        UserDetails userFromDB_username = userService.loadUserByUsername(user.getUsername());

        if (userFromDB_username != null) {
            model.addAttribute("usernameError", "Дане ім'я вже заняте!");
            return "tempNoAuthorization/registration";
        }

        if (user.getPassword().equals(user.getPassword().toLowerCase())) {
            model.addAttribute("passwordError", "Пароль повинен містити хоч одну велику літеру");
            return "tempNoAuthorization/registration";
        }

        if (user.getPassword().equals(user.getPassword().toUpperCase())) {
            model.addAttribute("passwordError", "Пароль повинен містити хоч одну маленьку літеру");
            return "tempNoAuthorization/registration";
        }

        //---------------------------------

        Set<Contradictions> contras = new HashSet<>();

        for (Contradictions contradiction : Contradictions.values()) {
            if (form.containsKey(contradiction.name()))
                contras.add(contradiction);
        }

        customUserDetails.setContradictions(contras);

        //---------------------------------


        user.setActive(false);
        customUserDetails.setActivationCode(UUID.randomUUID().toString());
        if (customUserDetails.getNutritionStyle() == NutritionStyle.SPORT) {
            customUserDetails.setBodyConstitution(BodyConstitution.DEFAULT);
            customUserDetails.setTrainType(TrainType.DEFAULT);
        }
        user.setCustomUserDetails(customUserDetails);

        String message = String.format("Вітаємо, %s! \n" +
        "Ви зареєструвалися на сайті Andrew`s Food. Щоб підтвердити реєстрацію, перейдіть за посиланням: " + "http://localhost:8090/registration/activate/%s",
                user.getUsername(),
                user.getCustomUserDetails().getActivationCode());
        mailService.send(user.getCustomUserDetails().getEmail(), "код активації", message);

        userRepo.save(user);

        return "redirect:/";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        if (userService.activateUser(code)){
            model.addAttribute("activateMessage", "Активаційний код підтверджено!");
            model.addAttribute("statusMessage", "true");
        }
        else {
            model.addAttribute("activateMessage", "Помилка при підтвердженні активаційного коду!");
            model.addAttribute("statusMessage", "false");
        }
        return "tempNoAuthorization/login";
    }
}
