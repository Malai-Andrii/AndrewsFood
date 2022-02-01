package com.site.andrewsfood.Controller.controllers;

import com.site.andrewsfood.Controller.Utilities.ControllerUtils;
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
public class ReceiptDayController {
    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private DishService dishService;

    @Autowired
    private UserService userService;

    @GetMapping("/mainUser/receiptDay")
    public String receiptDayGet(Authentication authentication, Model model) {
        User user = userService.findUserByUsername(authentication.getName());
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        model.addAttribute("categories", ingredientService.getCategoryList());
        return "tempUser/receiptDay";
    }

    @PostMapping("/mainUser/receiptDay")
    public String receiptDayPost(Authentication authentication,
                                 @RequestParam(name = "dishCount") Integer dishCount,
                                 @RequestParam(value = "categoryDouble") String categoryDouble,
                                 @RequestParam(value = "proteinWindow", required = false) String proteinWindow,
                                 @RequestParam(value = "enablePLC", required = false) String enablePLC,
                                 @RequestParam(value = "overwritePLC", required = false) String overwritePLC,
                                 @RequestParam(value = "overwriteCalority", required = false) String overwriteCalority,
                                 @RequestParam(value = "timeRestrict", required = false) String timeRestrict,
                                 @RequestParam(name = "proteinsCoef", required = false) Integer proteinsCoef,
                                 @RequestParam(name = "lipidsCoef", required = false) Integer lipidsCoef,
                                 @RequestParam(name = "carboCoef", required = false) Integer carboCoef,
                                 @RequestParam(name = "sugarsCoef", required = false) Integer sugarsCoef,
                                 @RequestParam(name = "newCalority", required = false) Integer newCalority,
                                 @RequestParam(name = "timeRestrictHour", required = false) Integer hours,
                                 @RequestParam(name = "timeRestrictMinute", required = false) Integer minutes,
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
                String ingredName = fullIngredName.substring(0, fullIngredName.indexOf('('));
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
                        return "tempUser/receiptDay";
                    }
                    amounts.add(amount);
                } catch (NumberFormatException ne) {
                    model.addAttribute("ingredientsError", "Вкажіть кількість всіх інгредієнтів, а також перевірте їх правильне написання!");
                    model.addAttribute("categories", ingredientService.getCategoryList());
                    model.addAttribute("ingredients", ingredientService.getAllIngredients());
                    return "tempUser/receiptDay";
                }
            }
        }


        for (int i = 0; i < ingredNames.size(); i++) {
            ingredientsListCustom.put(ingredNames.get(i), amounts.get(i));
        }

        // Dishes Selection from DB
        List<Dish> dishesAll = dishService.getAllDishes();

        CustomUserDetails userDetails = user.getCustomUserDetails();
        Set<Contradictions> contras = userDetails.getContradictions();
        String nutritionStyle = userDetails.getNutritionStyle();
        String userReligion = userDetails.getReligion();
        loop:
        for (int i=0; i < dishesAll.size(); ) {
            // Filtrating by contradictions
            Dish currentDish = dishesAll.get(i);

            if (contras.contains(currentDish.getDishContradictions())) {
                dishesAll.remove(i);
                continue;
            }
            // Filter by vegan, vegetarian etc.
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
            else if (!nutritionStyle.equals("звичайний") && !nutritionStyle.equals("спорт")) {
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
            if (userReligion.equals("іслам")) {
                for (String key : keyIngred) {
                    Ingredient ingred = ingredientService.findByIngredientName(key);
                    if (ingred.getIngredientName().toLowerCase().contains("свин")) {
                        dishesAll.remove(i);
                        continue loop;
                    };
                }
            }
            else if (userReligion.equals("юдаїзм")) {
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
            else if (userReligion.equals("індуїзм")) {
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
        // Now you have valid list with dishes

        // Set time limitation, if needed
        if (timeRestrict != null) {
            Iterator<Dish> dishIterator = dishesAll.iterator();
            while (dishIterator.hasNext()) {
                String cookTime = dishIterator.next().getCookTime();
                int currentHours = Integer.parseInt(cookTime.substring(0, cookTime.lastIndexOf(':')));
                int currentMinutes = Integer.parseInt(cookTime.substring(cookTime.lastIndexOf(':')));
                if (hours < currentHours || (hours == currentHours && minutes < currentMinutes)) {
                    dishIterator.remove();
                }
            }
        }

        // Finally, filtrate by ingredients

        for (int i = 0; i < dishesAll.size(); ) {
            Iterator<Dish> dishIterator = dishesAll.iterator();
            while(dishIterator.hasNext()) {
                Dish dish = dishIterator.next();
                Set<String> dishNames = dish.getIngredientList().keySet();
                for (String dishName : dishNames) {
                    if (!ingredientsListCustom.containsKey(dishName) ||
                            (dish.getIngredientList().get(dishName) > ingredientsListCustom.get(dishName))) {
                        dishIterator.remove();
                    }
                }
            }
        }

        // Now we make result list

        // proteinWindow categoryDouble dishCount enablePLC overwritePLC

        double proteinsNeed, lipidsRestrict, sugarsNeed;
        double proteins, lipids, carbo, sugars, mass, PLAberation, PCAberation, LCAberation, sugarRestrict;
        int caloryNeed;
        if (overwriteCalority != null) {
          caloryNeed = newCalority;
        }
        else {
            caloryNeed = userDetails.getCalority();
        }

        List<Dish> resultList = new LinkedList<>();
        int[] arr = null;

        // Adding protein window

        if (proteinWindow != null) {
            dishCount--;
            for (int i = 0; i < dishesAll.size(); ) {
                proteinsNeed = userDetails.getWeight() / 2;
                sugarsNeed = (proteinsNeed / 6) * 4;
                lipidsRestrict = proteinsNeed * 0.2;
                mass = dishesAll.get(i).getDishMass() / 100;
                proteins = dishesAll.get(i).getDishProteins() * mass;
                lipids = dishesAll.get(i).getDishLipids() * mass;
                carbo = dishesAll.get(i).getDishCarbo() * mass;
                sugars = dishesAll.get(i).getDishSugars() * mass;
                if (proteinsNeed > proteins && lipids < lipidsRestrict && (carbo * 0.7) < sugars && sugars > sugarsNeed) {
                    dishesAll.remove(i);
                    break;
                }
                double tempProteins = proteins;
                double tempMass = mass;
                while (tempProteins / proteinsNeed > 2) {
                    tempProteins -= proteins * 0.2;
                    tempMass -= mass * 0.2;
                }
                Dish pcw = dishesAll.get(i);
                pcw.setDishMass(tempMass);
                resultList.add(pcw);
                i++;
            }
        }

        // Making dish list


        if (enablePLC == null) {
            loop_2:
            while ((arr = ControllerUtils.generateCombinations(arr, dishCount, dishesAll.size())) != null) {
                double stackCalority, stackMass;
                stackCalority = stackMass = 0.0;
                Set<String> dishType = new HashSet<>();
                for (int k = 0; k < arr.length; k++) {
                    Dish currentDish;
                    currentDish = dishesAll.get(arr[k]);
                    if (categoryDouble != null) {
                        if (!dishType.contains(currentDish.getDishType())) {
                            dishType.add(currentDish.getDishType());
                        } else {
                            continue loop_2;
                        }
                    }
                    double dishMass = currentDish.getDishMass();
                    stackCalority += currentDish.getDishCalority() * (dishMass / 100);
                    stackMass += dishMass;
                }

                if (caloryNeed < stackCalority) {
                    continue loop_2;
                } else if (stackCalority >= caloryNeed && stackCalority <= (caloryNeed + caloryNeed * 0.05)) {
                    for (int i : arr) {
                        resultList.add(dishesAll.get(i));
                    }
                    break loop_2;
                } else {
                    List<double[]> listWages = ControllerUtils.generateWages(dishCount);
                    for (int i = 0; i < listWages.size(); i++) {
                        double[] arrWages = listWages.get(i);
                        stackCalority = stackMass = 0.0;
                        for (int k = 0; k < arr.length; k++) {
                            Dish currentDish;
                            currentDish = dishesAll.get(arr[k]);
                            double dishMass = currentDish.getDishMass() * arrWages[k];
                            stackCalority += currentDish.getDishCalority() * (dishMass / 100);
                            stackMass += dishMass;
                        }
                        if (stackCalority >= caloryNeed && stackCalority <= (caloryNeed + caloryNeed * 0.05)) {
                            int k = 0;
                            for (int o : arr) {
                                Dish currentDish;
                                currentDish = dishesAll.get(o);
                                currentDish.setDishMass(currentDish.getDishMass() * arrWages[k]);
                                k++;
                                resultList.add(currentDish);
                            }
                            break loop_2;
                        }
                    }
                }
            }
        }
        else {
            loop_2:
            while ((arr = ControllerUtils.generateCombinations(arr, dishCount, dishesAll.size())) != null) {
                double stackCalority, stackProteins, stackLipids, stackCarbo, stackSugars, stackMass;
                stackCalority = stackProteins = stackLipids = stackCarbo = stackSugars = stackMass = 0.0;
                Set<String> dishType = new HashSet<>();
                for (int k = 0; k < arr.length; k++) {
                    Dish currentDish;
                    currentDish = dishesAll.get(arr[k]);
                    if (categoryDouble != null) {
                        if (!dishType.contains(currentDish.getDishType())) {
                            dishType.add(currentDish.getDishType());
                        } else {
                            continue loop_2;
                        }
                    }
                    double dishMass = currentDish.getDishMass();
                    stackCalority += currentDish.getDishCalority() * (dishMass / 100);
                    stackProteins += currentDish.getDishProteins() * (dishMass / 100);
                    stackLipids += currentDish.getDishLipids() * (dishMass / 100);
                    stackCarbo += currentDish.getDishCarbo() * (dishMass / 100);
                    stackSugars += currentDish.getDishSugars() * (dishMass / 100);
                    stackMass += dishMass;
                }

                List<double[]> listWages = ControllerUtils.generateWages(dishCount);
                loop_3:
                for (int i = 0; i < listWages.size(); i++) {
                    double[] arrWages = listWages.get(i);
                    stackCalority = stackProteins = stackLipids = stackCarbo = stackSugars = stackMass = 0.0;
                    for (int k = 0; k < arr.length; k++) {
                        Dish currentDish;
                        currentDish = dishesAll.get(arr[k]);
                        if (categoryDouble != null) {
                            if (!dishType.contains(currentDish.getDishType())) {
                                dishType.add(currentDish.getDishType());
                            } else {
                                continue loop_2;
                            }
                        }
                        double dishMass = currentDish.getDishMass() * arrWages[k];
                        stackCalority += currentDish.getDishCalority() * (dishMass / 100);
                        stackMass += dishMass;
                    }

                    PLAberation = Math.abs(1 - (stackProteins / stackLipids) / (proteinsCoef / lipidsCoef));
                    PCAberation = Math.abs(1 - (stackProteins / stackCarbo) / (proteinsCoef / carboCoef));
                    LCAberation = Math.abs(1 - (stackLipids / stackCarbo) / (lipidsCoef / carboCoef));
                    sugarRestrict = (sugarsCoef / 4) * (stackCalority / 100);
                    if (PLAberation < 0.1 || PCAberation < 0.1 || LCAberation < 0.1 || sugarRestrict < stackSugars ||
                            (stackCalority >= caloryNeed && stackCalority <= (caloryNeed + caloryNeed * 0.05))) {
                        continue loop_3;
                    } else {
                        int k = 0;
                        for (int o : arr) {
                            Dish currentDish;
                            currentDish = dishesAll.get(o);
                            currentDish.setDishMass(currentDish.getDishMass() * arrWages[k]);
                            k++;
                            resultList.add(currentDish);
                        }
                        break loop_2;
                    }
                }
            }
        }

//            if (enablePLC != null) {
//                List<double[]> listWages = ControllerUtils.generateWages(dishCount);
//                stackCalority = stackMass = stackProteins = stackLipids = stackCarbo = stackSugars = 0.0;
//                for (int i = 0; i < arr.length; i++) {
//                    double[] arrWages = listWages.get(i);
//                    for (int k = 0; k < arr.length; k++) {
//                        Dish currentDish;
//                        currentDish = dishesAll.get(arr[k]);
//                        double dishMass = currentDish.getDishMass() * arrWages[k];
//                        stackCalority += currentDish.getDishCalority() * (dishMass / 100);
//                        stackProteins += currentDish.getDishProteins() * (dishMass / 100);
//                        stackLipids += currentDish.getDishLipids() * (dishMass / 100);
//                        stackCarbo += currentDish.getDishCarbo() * (dishMass / 100);
//                        stackSugars += currentDish.getDishSugars() * (dishMass / 100);
//                        stackMass += dishMass;
//                    }

//                }
//            }
//        }

        model.addAttribute("resultDishList", resultList);
        return "tempUser/resultDishListDay";
    }
}
