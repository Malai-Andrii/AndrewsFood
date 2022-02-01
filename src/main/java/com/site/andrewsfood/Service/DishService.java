package com.site.andrewsfood.Service;


import com.site.andrewsfood.Model.domain.Dish;
import com.site.andrewsfood.Dao.DishRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {

    private final DishRepo dishRepo;

    public DishService(DishRepo dishRepo) {
        this.dishRepo = dishRepo;
    }

    public void saveDish(Dish dish) {dishRepo.save(dish);}

    public void deleteAll() {dishRepo.deleteAll();}

    public List<String> getTypeList(){
        return dishRepo.getTypeList();
    }

    public Dish findByDishName(String dishName){
        return dishRepo.findByDishName(dishName);
    }

    public Dish findById(long id){
        return dishRepo.findById(id);
    }

    public List<Dish> getAllDishes() {
        return dishRepo.findAll();
    }

}
