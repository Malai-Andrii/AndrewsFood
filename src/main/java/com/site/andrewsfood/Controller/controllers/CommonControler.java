package com.site.andrewsfood.Controller.controllers;

import com.site.andrewsfood.Service.DishService;
import com.site.andrewsfood.Service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommonControler {

    private final IngredientService ingredientService;
    private final DishService dishService;

    CommonControler(IngredientService ingredientService, DishService dishService) {
        this.ingredientService = ingredientService;
        this.dishService = dishService;
    }

    @GetMapping("/allIngredients")
    public String allIngredientsGet(Model model) {
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        model.addAttribute("categories", ingredientService.getCategoryList());
        return "allIngredients";
    }

    @PostMapping("/allIngredients")
    public String allIngredientsPost(Model model) {
        return "allIngredients";
    }

    @GetMapping("/allDishes")
    public String allDishGet(Model model) {
        model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("types", dishService.getTypeList());
        return "allDishes";
    }

    @PostMapping("/allDishes")
    public String allDishPost(Model model) {
        return "allDishes";
    }

    @GetMapping("/allDishes/{dishId}")
    public String allDishGet(@PathVariable("dishId") long dishId, Model model) {
        model.addAttribute("dishes", dishService.findById(dishId));
        return "allDishes";
    }
}
