package com.site.andrewsfood.Dao;

import com.site.andrewsfood.Model.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientRepo extends JpaRepository<Ingredient, Long> {
    Ingredient findByIngredientName(String ingredientName);
    List<Ingredient> findAll();
    @Query("select distinct i.category from Ingredient i")
    List<String> getCategoryList();
    @Query("select i.ingredientName from Ingredient i")
    List<String> findAllNames();
}
