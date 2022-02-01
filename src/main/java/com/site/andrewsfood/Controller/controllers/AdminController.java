package com.site.andrewsfood.Controller.controllers;

import com.site.andrewsfood.Controller.Utilities.ControllerUtils;
import com.site.andrewsfood.Controller.Utilities.FileUploadUtils;
import com.site.andrewsfood.Model.domain.Contradictions;
import com.site.andrewsfood.Model.domain.Dish;
import com.site.andrewsfood.Model.domain.Ingredient;
import com.site.andrewsfood.Dao.DishRepo;
import com.site.andrewsfood.Dao.IngredientRepo;
import com.site.andrewsfood.Service.DishService;
import com.site.andrewsfood.Service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mainAdmin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private DishRepo dishRepo;
    private IngredientRepo ingredientRepo;
    private DishService dishService;
    private IngredientService ingredientService;

    public AdminController(DishRepo dishRepo, IngredientRepo ingredientRepo,
                           DishService dishService, IngredientService ingredientService) {
        this.dishRepo = dishRepo;
        this.ingredientRepo = ingredientRepo;
        this.dishService = dishService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public String mainAdminGet(Model model) {
        return "tempAdmin/mainAdmin";
    }

    @GetMapping("/addIngredient")
    public String addIngredientGet(Model model) {
        model.addAttribute("categories", ingredientService.getCategoryList());
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        model.addAttribute("dishTypes", dishService.getTypeList());
        return "tempAdmin/addIngredient";
    }

    @PostMapping("/addIngredient")
    public String addIngredientPost(@Valid Ingredient ingredient, BindingResult bindingResult,
                                    @RequestParam("imageIngr") MultipartFile multipartFile, Model model,
                                    @RequestParam Map<String, String> form) throws IOException {

        if (ingredient.getCategory().equals("Різне")) {
            ingredient.setCalority(0.0);
            ingredient.setProteins(0.0);
            ingredient.setLipids(0.0);
            ingredient.setCarbo(0.0);
            ingredient.setSugars(0.0);
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "tempAdmin/addIngredient";
        }

        Ingredient ingredientFromDB = ingredientService.findByIngredientName(ingredient.getIngredientName());

        if (ingredientFromDB != null) {
            model.addAttribute("ingredientNameError", "Даний інгредієнт вже існує!");
            return "tempAdmin/addIngredient";
        }

        boolean isIncorrectNumberField = false;
        if (ingredient.getCalority() == null) { model.addAttribute("calorityError", "Вкажіть калорійність!"); isIncorrectNumberField = true;}
        if (ingredient.getProteins() == null) { model.addAttribute("proteinsError", "Вкажіть вміст білків!"); isIncorrectNumberField = true;}
        if (ingredient.getLipids() == null) { model.addAttribute("lipidsError", "Вкажіть вміст жирів!"); isIncorrectNumberField = true;}
        if (ingredient.getCarbo() == null) { model.addAttribute("carboError", "Вкажіть вміст вуглеводів!"); isIncorrectNumberField = true;}
        if (ingredient.getSugars() == null) { model.addAttribute("sugarsError", "Вкажіть вміст цукрів!"); isIncorrectNumberField = true;}

        if (isIncorrectNumberField) {
            return "tempAdmin/addIngredient";
        }

        if (ingredient.getCarbo() < ingredient.getSugars()) {
            model.addAttribute("sugarsError", "Цукрів не може бути більше вуглеводів, це їх складові!");
            return "tempAdmin/addIngredient";
        }

        //---------------------------------

        Set<Contradictions> contras = new HashSet<Contradictions>();

        Set<String> contradictions = Arrays.stream(Contradictions.values())
                .map(Contradictions::name)
                .collect(Collectors.toSet());

        for (String key : form.keySet()) {
            if (contradictions .contains(key)) contras.add(Contradictions.valueOf(key));
        }

        ingredient.setIngredientContradictions(contras);

        //---------------------------------

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        ingredient.setFileImg(fileName);

        Ingredient savedIngredient = ingredientRepo.save(ingredient);
        model.addAttribute("successSave", "Інгредієнт успішно збережено!");

        String uploadDir = "src/main/resources/photos/ingredient-photos/" + savedIngredient.getId();
        FileUploadUtils.saveFile(uploadDir, fileName, multipartFile);

        return "tempAdmin/addIngredient";
    }

    @GetMapping("/addDish")
    public String addDishGet(Model model) {
        model.addAttribute("categories", ingredientService.getCategoryList());
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        return "tempAdmin/addDish";
    }

    @PostMapping("/addDish")
    public String addDishPost(@Valid Dish dish, BindingResult bindingResult, @RequestParam("cookTimeHour") String hours,
                              @RequestParam("cookTimeMinute") String minutes, @RequestParam("imageIngr") MultipartFile multipartFile,
                              Model model, @RequestParam Map<String, String> form) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            for (String error : errors.keySet()) {
                System.out.println(errors.get(error));
            }
            model.mergeAttributes(errors);
            model.addAttribute("categories", ingredientService.getCategoryList());
            model.addAttribute("ingredients", ingredientService.getAllIngredients());
            return "tempAdmin/addDish";
        }

        Dish dishFromDB = dishService.findByDishName(dish.getDishName());

        if (dishFromDB != null) {
            model.addAttribute("dishNameError", "Дана страва вже є!");
            model.addAttribute("categories", ingredientService.getCategoryList());
            model.addAttribute("ingredients", ingredientService.getAllIngredients());
            return "tempAdmin/addDish";
        }

        if (dish.getDishType().equals("null")) {
            model.addAttribute("dishTypeError", "Оберіть категорію страви!");
            model.addAttribute("categories", ingredientService.getCategoryList());
            model.addAttribute("ingredients", ingredientService.getAllIngredients());
            return "tempAdmin/addDish";
        }
        if(dish.getDishLitre() == null) {
            dish.setDishLitre(0.0);
        }
        dish.setCookTime(hours + ":" + minutes);

        //---------------------------------

        List<String> ingredNames = new ArrayList<String>();
        List<Double> amounts = new ArrayList<Double>();
        Map<String, Double> ingredientsForm = new HashMap<>();
        Set<Contradictions> contras = new HashSet<Contradictions>();
        dish.setDishContradictions(contras);
        for (String key : form.keySet()) {
            // get names
            if (key.startsWith("ingredient_text_")) {
                String fullIngredName = form.get(key);
                String ingredName = fullIngredName.substring(0, fullIngredName.indexOf('(') - 1);
                ingredNames.add(ingredName);
                Ingredient ingredient = ingredientService.findByIngredientName(ingredName);
                Set<Contradictions> currentContras = ingredient.getIngredientContradictions();
                contras.addAll(currentContras);
            }
            // get amount
            else if (key.startsWith("ingredient_") && !key.startsWith("ingredient_name_")) {
                try {
                    double amount = Double.parseDouble(form.get(key));
                    if (amount <= 0.0) {
                        model.addAttribute("ingredientsError", "Маса інгредієнтів не може бути менша чи рівня нулю!");
                        model.addAttribute("categories", ingredientService.getCategoryList());
                        model.addAttribute("ingredients", ingredientService.getAllIngredients());
                        return "tempAdmin/addDish";
                    }
                    amounts.add(amount);
                } catch (NumberFormatException ne) {
                    model.addAttribute("ingredientsError", "Вкажіть кількість всіх інгредієнтів, а також перевірте їх правильне написання!");
                    model.addAttribute("categories", ingredientService.getCategoryList());
                    model.addAttribute("ingredients", ingredientService.getAllIngredients());
                    return "tempAdmin/addDish";
                }
            }
        }


        for (int i = 0; i < ingredNames.size(); i++) {
            ingredientsForm.put(ingredNames.get(i), amounts.get(i));
        }

        dish.setIngredientList(ingredientsForm);

        //---------------------------------

        Set<String> contradictions = Arrays.stream(Contradictions.values())
                .map(Contradictions::name)
                .collect(Collectors.toSet());


        for (String key : form.keySet()) {
            if (contradictions .contains(key)) {
                dish.getDishContradictions().add(Contradictions.valueOf(key));
            }
        }



        //---------------------------------

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        dish.setFileImg(fileName);

        double currentCalority, currentProteins, currentLipids, currentCarbo, currentSugars, mass;
        currentCalority = currentProteins = currentLipids = currentCarbo = currentSugars = mass = 0;

        for (int i = 0; i < ingredNames.size(); i++) {
            Ingredient ingredient = ingredientService.findByIngredientName(ingredNames.get(i));
            double ingredientAmount = amounts.get(i);
            currentCalority += ingredient.getCalority() * (ingredientAmount / 100);
            currentProteins += ingredient.getProteins() * (ingredientAmount / 100);
            currentLipids += ingredient.getLipids() * (ingredientAmount / 100);
            currentCarbo += ingredient.getCarbo() * (ingredientAmount / 100);
            currentSugars += ingredient.getSugars() * (ingredientAmount / 100);
            mass += amounts.get(i);
        }
        if (dish.getDishType().equals("супи")) {
            mass += dish.getDishLitre() * 1000;
        }
        currentCalority = currentCalority / (mass / 100);
        currentProteins = currentProteins / (mass / 100);
        currentLipids = currentLipids / (mass / 100);
        currentCarbo = currentCarbo / (mass / 100);
        currentSugars = currentSugars / (mass / 100);

        dish.setDishCalority(Math.round(currentCalority * 10.0) / 10.0);
        dish.setDishProteins(Math.round(currentProteins * 10.0) / 10.0);
        dish.setDishLipids(Math.round(currentLipids * 10.0) / 10.0);
        dish.setDishCarbo(Math.round(currentCarbo * 10.0) / 10.0);
        dish.setDishSugars(Math.round(currentSugars * 10.0) / 10.0);
        dish.setDishMass(mass);

        Dish savedDish = dishRepo.save(dish);
        model.addAttribute("successSave", "Страву успішно збережено!");

        String uploadDir = "src/main/resources/photos/dish-photos/" + savedDish.getId();
        FileUploadUtils.saveFile(uploadDir, fileName, multipartFile);

        model.addAttribute("categories", ingredientService.getCategoryList());
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        return "tempAdmin/addDish";
    }

    @GetMapping("/userOffer")
    public String userOfferGet(Model model) {
        return "tempAdmin/userOffer";
    }

    @PostMapping("/userOffer")
    public String userOfferPost(Model model) {
        return "tempAdmin/userOffer";
    }

}
