package com.site.andrewsfood.Controller.controllers;

import com.site.andrewsfood.Model.domain.*;
import com.site.andrewsfood.Service.DishService;
import com.site.andrewsfood.Service.IngredientService;
import com.site.andrewsfood.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;


@Controller
public class ReceiptSelectionController {
    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private DishService dishService;

    @Autowired
    private UserService userService;

    @GetMapping("/mainUser/receiptSelection")
    public String receiptSelectionGet(Authentication authentication, Model model) {
        User user = userService.findUserByUsername(authentication.getName());
        model.addAttribute("nutritionStyle", user.getCustomUserDetails().getNutritionStyle());
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        model.addAttribute("categories", ingredientService.getCategoryList());
        return "tempUser/receiptSelection";
    }

    @PostMapping("/mainUser/receiptSelection")
    public String receiptSelectionPost(Authentication authentication,
                                       @RequestParam(name = "proteinsCoef", required = false) Integer proteinsCoef,
                                       @RequestParam(name = "lipidsCoef", required = false) Integer lipidsCoef,
                                       @RequestParam(name = "carboCoef", required = false) Integer carboCoef,
                                       @RequestParam(name = "sugarsCoef", required = false) Integer sugarsCoef,
                                       @RequestParam(value = "timeRestriction", required = false) String timeRestriction,
                                       @RequestParam(name = "timeRestrictHour", required = false) Integer hours,
                                       @RequestParam(name = "timeRestrictMinute", required = false) Integer minutes,
                                       @RequestParam(value = "selectionMode") String selectionMode,
                                       @RequestParam(value = "usePLC", required = false) String usePLC,
                                       @RequestParam(value = "proteinWindow", required = false) String proteinWindow,
                                       @RequestParam Map<String, String> form, Model model) {
        User user = userService.findUserByUsername(authentication.getName());

        // Get ingredients
        List<String> ingredNames = new ArrayList<String>();
        List<Double> amounts = new ArrayList<Double>();
        Map<String, Double> ingredientsListCustom = new HashMap<>();
        for (String key : form.keySet()) {
            // get names
            if (key.startsWith("ingredient_text_")) {
                String fullIngredName = form.get(key);
                String ingredName = fullIngredName.substring(0, fullIngredName.indexOf('(') - 1);
                ingredNames.add(ingredName);
            }

            // get amount

            else if (key.startsWith("ingredient_") && !key.startsWith("ingredient_name_")) {
                try {
                    Double amount = Double.parseDouble(form.get(key));
                    if (amount <= 0.0) {
                        model.addAttribute("ingredientsError", "Маса інгредієнтів не може бути менша чи рівня нулю!");
                        model.addAttribute("categories", ingredientService.getCategoryList());
                        model.addAttribute("ingredients", ingredientService.getAllIngredients());
                        return "tempUser/receiptSelection";
                    }
                    amounts.add(amount);
                } catch (NumberFormatException ne) {
                    model.addAttribute("ingredientsError", "Вкажіть кількість всіх інгредієнтів, а також перевірте їх правильне написання!");
                    model.addAttribute("categories", ingredientService.getCategoryList());
                    model.addAttribute("ingredients", ingredientService.getAllIngredients());
                    return "tempUser/receiptSelection";
                }
            }
        }


        for (int i = 0; i < ingredNames.size(); i++) {
            ingredientsListCustom.put(ingredNames.get(i), amounts.get(i));
        }

        // Dishes Selection from DB
        List<Dish> dishesAll = dishService.getAllDishes();

        Set<Contradictions> contras = user.getCustomUserDetails().getContradictions();
        Set<Contradictions> dishContras = new HashSet<Contradictions>();
        String nutritionStyle;
        loop:
        for (int i=0; i < dishesAll.size(); ) {
            // Filtrating by contradictions

            Dish currentDish = dishesAll.get(i);
            dishContras = currentDish.getDishContradictions();

            if (contras.contains(dishContras)) {
                dishesAll.remove(i);
                continue;
            }
            // Filter by vegan, vegetarian etc.
            nutritionStyle = user.getCustomUserDetails().getNutritionStyle();
            Map <String, Double> currentIngredients = currentDish.getIngredientList();
            Set<String> keyIngred = currentIngredients.keySet();
            if (nutritionStyle.equals("редутаріанець")) {
                double wholeMass = 0.0;
                double meatMass = 0.0;
                for (String key : keyIngred) {
                    wholeMass += currentIngredients.get(key);
                    if (ingredientService.findByIngredientName(key).getCategory().equals("м'ясні продукти")) {
                        meatMass += currentIngredients.get(key);
                    }
                }
                if ((meatMass / wholeMass) > 0.1) {
                    dishesAll.remove(i);
                    continue loop;
                }
            }
            else if (!nutritionStyle.equals("звичайний") || !nutritionStyle.equals("спорт")) {
                for (String key : keyIngred) {
                    Ingredient ingred = ingredientService.findByIngredientName(key);
                    if (ingred.getCategory().equals("м`ясні продукти") && nutritionStyle.equals("вегетеріанець")) {
                        dishesAll.remove(i);
                        continue loop;
                    } else if ((ingred.getCategory().equals("м`ясні продукти") || ingred.getCategory().equals("молочні продукти") ||
                            ingred.getCategory().equals("яйця")) && nutritionStyle.equals("веган")) {
                        dishesAll.remove(i);
                        continue loop;
                    }
                }
            }

            // Filtrating by religion.
            String userReligion = user.getCustomUserDetails().getReligion();
            if (userReligion.equals("іслам")) {
                for (String key : keyIngred) {
                    Ingredient ingred = ingredientService.findByIngredientName(key);
                    if (ingred.getIngredientName().toLowerCase().contains("свин")) {
                        dishesAll.remove(i);
                        continue loop;
                    };
                }
            }
            if (userReligion.equals("юдаїзм")) {
                boolean containsMilk = false;
                boolean containsMeat = false;
                for (String key : keyIngred) {
                    Ingredient ingred = ingredientService.findByIngredientName(key);
                    if (ingred.getIngredientName().toLowerCase().contains("свин")) {
                        dishesAll.remove(i);
                        continue loop;
                    };
                    if (ingred.getCategory().equals("молочні продукти")){
                        containsMilk = true;
                    }
                    if (ingred.getCategory().equals("м`ясні продукти")){
                        containsMeat = true;
                    }
                    if (containsMeat && containsMilk) {
                        dishesAll.remove(i);
                        continue loop;
                    }
                }
            }
            if (userReligion.equals("індуїзм")) {
                for (String key : keyIngred) {
                    Ingredient ingred = ingredientService.findByIngredientName(key);
                    if (ingred.getIngredientName().toLowerCase().contains("теля") ||
                            ingred.getIngredientName().toLowerCase().contains("ялови") ||
                            ingred.getIngredientName().toLowerCase().contains("коров")) {
                        dishesAll.remove(i);
                        continue loop;
                    };
                }
            }
            i++;
        }
         //Now you have valid list with dishes

         //Set time limitation, if needed
        if (timeRestriction != null) {
            for (int i = 0; i < dishesAll.size(); ) {
                String cookTime = dishesAll.get(i).getCookTime();
                int currentHours = Integer.parseInt(cookTime.substring(0, cookTime.lastIndexOf(':')));
                int currentMinutes = Integer.parseInt(cookTime.substring(cookTime.lastIndexOf(':')));
                if (hours < currentHours) {
                    dishesAll.remove(i);
                    continue;
                }
                else if (hours == currentHours && minutes < currentMinutes) {
                    dishesAll.remove(i);
                    continue;
                }
                i++;
            }
        }


        // Finally, filtrate by ingredients
        if (selectionMode.equals("full")) {
            loop_2:
            for (int i = 0; i < dishesAll.size(); ) {
                Dish dish = dishesAll.get(i);
                Set<String> dishNames = dish.getIngredientList().keySet();
                Set<String> ingredSet = ingredientsListCustom.keySet();
                if (!ingredSet.containsAll(dishNames)) {
                    dishesAll.remove(i);
                    continue loop_2;
                }
                for (String dishName : dishNames) {
                    System.out.println(dish.getIngredientList().get(dishName));
                    System.out.println(ingredientsListCustom.get(dishName));
                    if (dish.getIngredientList().get(dishName) > ingredientsListCustom.get(dishName)) {
                        dishesAll.remove(i);
                        continue loop_2;
                    }
                }
                i++;
            }
        }
        else {
            loop_2:
            for (int i = 0; i < dishesAll.size(); ) {
                Dish dish = dishesAll.get(i);
                Set<String> ingredSet = ingredientsListCustom.keySet();
                Set<String> dishNames = dish.getIngredientList().keySet();
                boolean isSpare = false;
                for (String dishName : dishNames) {
                    if (!ingredSet.contains(dishName)) {
                        if (isSpare) {
                            isSpare = true;
                            continue loop_2;
                        }
                        else {
                            dishesAll.remove(i);
                            continue loop_2;
                        }
                    } else if (dish.getIngredientList().get(dishName) > ingredientsListCustom.get(dishName)) {
                        dishesAll.remove(i);
                        continue loop_2;
                    }
                }
                i++;
            }
        }
        // and then by PLC, if needed

        double proteinsNeed, lipidsRestrict, sugarsNeed;
        double proteins, lipids, carbo, sugars, basicNumber, PLAberation, PCAberation, LCAberation, sugarRestrict;

        if (proteinWindow != null) {
            for (int i = 0; i < dishesAll.size(); ) {
            proteinsNeed = user.getCustomUserDetails().getWeight() / 2;
            sugarsNeed = (proteinsNeed / 3) * 2;
            lipidsRestrict = proteinsNeed * 0.2;
            proteins = dishesAll.get(i).getDishProteins();
            lipids = dishesAll.get(i).getDishLipids();
            carbo = dishesAll.get(i).getDishCarbo();
            sugars = dishesAll.get(i).getDishSugars();
            if (proteinsNeed < proteins || lipids > lipidsRestrict || (carbo * 0.7) > sugars || sugars < sugarsNeed) {
                dishesAll.remove(i);
                continue;
            }
            i++;
            }
        }
        else if (usePLC != null) {
            loop_3:
            for (int i = 0; i < dishesAll.size(); ) {
                proteins = dishesAll.get(i).getDishProteins();
                lipids = dishesAll.get(i).getDishLipids();
                carbo = dishesAll.get(i).getDishCarbo();
                sugars = dishesAll.get(i).getDishSugars();


                PLAberation =  Math.abs(1 - (proteins / lipids) / ((double)proteinsCoef / (double)lipidsCoef));
                PCAberation = Math.abs(1 - (proteins / carbo) / ((double)proteinsCoef / (double)carboCoef));
                LCAberation = Math.abs(1 - (lipids / carbo) / ((double)lipidsCoef / (double)carboCoef));
                sugarRestrict = ((double)sugarsCoef / 4) * (dishesAll.get(i).getDishCalority() / 100);

                if (PLAberation > 0.15 || PCAberation > 0.15 || LCAberation > 0.15 || sugars > sugarRestrict) {
                    dishesAll.remove(i);
                    continue loop_3;
                }
                i++;
            }
        }

        // return dishes
        model.addAttribute("resultDishes", dishesAll);
        return "tempUser/recievedSelection";
    }
}
