package com.site.andrewsfood.Service;

import com.site.andrewsfood.Model.domain.Dish;
import com.site.andrewsfood.Model.domain.Ingredient;
import com.site.andrewsfood.Dao.IngredientRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepo ingredientRepo;

    public IngredientService(IngredientRepo ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    public void saveIngredient(Ingredient ingredient) {ingredientRepo.save(ingredient);}

    public void deleteAll() {ingredientRepo.deleteAll();}

    public List<String> findAllNames(){
        return ingredientRepo.findAllNames();
    };

    public List<Ingredient> getAllIngredients(){
        return ingredientRepo.findAll();
    };

    public Ingredient findById(long id){
        return ingredientRepo.findById(id);
    }

    public Ingredient findByIngredientName(String ingredientName){
        return ingredientRepo.findByIngredientName(ingredientName);
    };

    public List<String> getCategoryList(){
        return ingredientRepo.getCategoryList();
    };
}
