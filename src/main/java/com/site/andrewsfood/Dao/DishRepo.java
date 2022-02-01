package com.site.andrewsfood.Dao;

import com.site.andrewsfood.Model.domain.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DishRepo extends JpaRepository<Dish, Long> {
    Dish findByDishName(String dishName);
    Dish findById(long Id);
    List<Dish> findAll();
    @Query("select distinct d.dishType from Dish d")
    List<String> getTypeList();
}
