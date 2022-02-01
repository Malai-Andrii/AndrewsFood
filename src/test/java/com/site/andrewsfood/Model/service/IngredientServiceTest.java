package com.site.andrewsfood.Model.service;

import com.site.andrewsfood.Model.domain.Contradictions;
import com.site.andrewsfood.Model.domain.Ingredient;
import com.site.andrewsfood.Dao.IngredientRepo;
import com.site.andrewsfood.Service.IngredientService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
class IngredientServiceTest {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepo ingredientRepo;

    @Test
    void findAllNames() {
        ingredientService.deleteAll();
        Set<Contradictions> contras = new HashSet();
        Ingredient ingred = new Ingredient("Огірок", "Овочі", 50.0,
                5.0, 5.6, 5.5, 5.5, "default.jpg", "грамів", contras);
        Ingredient ingred2 = new Ingredient("Перець", "Овочі", 50.0,
                5.0, 5.6, 5.5, 5.5, "default.jpg", "грамів", contras);
        Ingredient ingred3 = new Ingredient("Помідор", "Овочі", 50.0,
                5.0, 5.6, 5.5, 5.5, "default.jpg", "грамів", contras);
        ingredientService.saveIngredient(ingred);
        ingredientService.saveIngredient(ingred2);
        ingredientService.saveIngredient(ingred3);

        List<String> listNames = ingredientService.findAllNames();
        boolean match = listNames.contains("Огірок") && listNames.contains("Помідор") && listNames.contains("Перець");
        Assert.assertTrue(match);
    }

    @Test
    void getAllIngredients() {
        ingredientService.deleteAll();
        Set<Contradictions> contras = new HashSet();
        Ingredient ingred = new Ingredient("Огірок", "Овочі", 50.0,
                5.0, 5.6, 5.5, 5.5, "default.jpg", "грамів", contras);
        Ingredient ingred2 = new Ingredient("Перець", "Овочі", 50.0,
                5.0, 5.6, 5.5, 5.5, "default.jpg", "грамів", contras);
        Ingredient ingred3 = new Ingredient("Помідор", "Овочі", 50.0,
                5.0, 5.6, 5.5, 5.5, "default.jpg", "грамів", contras);
        ingredientService.saveIngredient(ingred);
        ingredientService.saveIngredient(ingred2);
        ingredientService.saveIngredient(ingred3);

        List<Ingredient> listIngreds = ingredientService.getAllIngredients();
        System.out.println(listIngreds.size());
        Assert.assertEquals(3, listIngreds.size());
    }

    @Test
    void getCategoryList() {
        ingredientService.deleteAll();
        Set<Contradictions> contras = new HashSet();
        Ingredient ingred = new Ingredient("Огірок", "Овочі", 50.0,
                5.0, 5.6, 5.5, 5.5, "default.jpg", "грамів", contras);
        Ingredient ingred2 = new Ingredient("Алича", "Фрукти", 50.0,
                5.0, 5.6, 5.5, 5.5, "default.jpg", "грамів", contras);
        Ingredient ingred3 = new Ingredient("Яблуко", "Фрукти", 50.0,
                5.0, 5.6, 5.5, 5.5, "default.jpg", "грамів", contras);
        ingredientService.saveIngredient(ingred);
        ingredientService.saveIngredient(ingred2);
        ingredientService.saveIngredient(ingred3);

        List<String> listCategories = ingredientService.getCategoryList();
        Assert.assertEquals(2, listCategories.size());

    }
}