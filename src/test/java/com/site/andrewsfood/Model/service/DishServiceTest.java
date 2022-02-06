package com.site.andrewsfood.Model.service;

import com.site.andrewsfood.Model.domain.enums.Contradictions;
import com.site.andrewsfood.Model.domain.Dish;
import com.site.andrewsfood.Service.DishService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class DishServiceTest {

    @Autowired
    private DishService dishService;

    @Test
    void getTypeList() {
        dishService.deleteAll();
        Set<Contradictions> contras = new HashSet();
        Map<String, Double> ingreds = new HashMap();
        Dish dish = new Dish("Страва", "1:30", "Приготування", "Лінки нема", 2.0, 500.0,
                ingreds, contras, "Риба", "default.jpg", 200.0, 10.0, 10.0, 10.0, 15.0);
        Dish dish2 = new Dish("Страва2", "1:30", "Приготування", "Лінки нема", 2.0, 500.0,
                ingreds, contras, "Риба", "default.jpg", 200.0, 10.0, 10.0, 10.0, 15.0);
        Dish dish3 = new Dish("Страва3", "1:30", "Приготування", "Лінки нема", 2.0, 500.0,
                ingreds, contras, "Салат", "default.jpg", 200.0, 10.0, 10.0, 10.0, 15.0);
        dishService.saveDish(dish);
        dishService.saveDish(dish2);
        dishService.saveDish(dish3);

        List<String> types = dishService.getTypeList();
        boolean match = types.contains("Риба") && types.contains("Салат");
        Assert.assertTrue(match);
    }

    @Test
    void findByDishName() {
        dishService.deleteAll();
        Set<Contradictions> contras = new HashSet();
        Map<String, Double> ingreds = new HashMap();
        Dish dish = new Dish("Страва", "1:30", "Приготування", "Лінки нема", 2.0, 500.0,
                ingreds, contras, "Риба", "default.jpg", 200.0, 10.0, 10.0, 10.0, 15.0);
        dishService.saveDish(dish);
        Dish dishRecieved = dishService.findByDishName("Страва");
        Assert.assertTrue(dishRecieved.getDishName().equals(dish.getDishName()));
    }

    @Test
    void getAllDishes() {
        dishService.deleteAll();
        Set<Contradictions> contras = new HashSet();
        Map<String, Double> ingreds = new HashMap();
        Dish dish = new Dish("Страва", "1:30", "Приготування", "Лінки нема", 2.0, 500.0,
                ingreds, contras, "Риба", "default.jpg", 200.0, 10.0, 10.0, 10.0, 15.0);
        Dish dish2 = new Dish("Страва2", "1:30", "Приготування", "Лінки нема", 2.0, 500.0,
                ingreds, contras, "Риба", "default.jpg", 200.0, 10.0, 10.0, 10.0, 15.0);
        Dish dish3 = new Dish("Страва3", "1:30", "Приготування", "Лінки нема", 2.0, 500.0,
                ingreds, contras, "Салат", "default.jpg", 200.0, 10.0, 10.0, 10.0, 15.0);
        dishService.saveDish(dish);
        dishService.saveDish(dish2);
        dishService.saveDish(dish3);

        List<Dish> allDishRecieved = dishService.getAllDishes();
        Assert.assertEquals(3, allDishRecieved.size());
    }
}