package com.site.andrewsfood.Service;

import com.site.andrewsfood.Model.domain.CustomUserDetails;
import com.site.andrewsfood.Model.domain.enums.BodyConstitution;
import com.site.andrewsfood.Model.domain.enums.NutritionStyle;
import com.site.andrewsfood.Model.domain.enums.Role;
import com.site.andrewsfood.Model.domain.User;
import com.site.andrewsfood.Dao.CustomUserDetailsRepo;
import com.site.andrewsfood.Dao.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final CustomUserDetailsRepo customUserDetailsRepo;

    public UserService(CustomUserDetailsRepo customUserDetailsRepo, UserRepo userRepo) {
        this.customUserDetailsRepo = customUserDetailsRepo;
        this.userRepo = userRepo;
    }

    public void saveUser(User user) {userRepo.save(user);}

    public void deleteAll() {userRepo.deleteAll();}

    public List<User> findAllUsers() {
        return userRepo.findAllByRoleUser();
    }

    public User findUserByUsername(String name) {
        return userRepo.findByUsername(name);
    }

    public User findUserById(long id) {
        return userRepo.findById(id);
    }

    public long allUsersCount() { return userRepo.countAllByUserRole(); }
    public Integer AverageAge() { return userRepo.AvgAge(); }
    public Integer AverageCalority() {
        return userRepo.AvgCalority();
    }
    public Integer AverageHeight() {
        return userRepo.AvgHeight();
    }
    public Integer AverageWeight() {
        return userRepo.AvgWeight();
    }
    public BodyConstitution CommonBodyConstitution() {
        Map<Integer, BodyConstitution> common = new HashMap<>();
        common.put(userRepo.EctoBodyConstitution(), BodyConstitution.ECTO);
        common.put(userRepo.MezoBodyConstitution(), BodyConstitution.MESO);
        common.put(userRepo.EndoBodyConstitution(), BodyConstitution.ENDO);
        int maxValue = Math.max(Math.max(userRepo.EctoBodyConstitution(),
                userRepo.MezoBodyConstitution()), userRepo.EndoBodyConstitution());
        return common.get(maxValue);
    }
    public NutritionStyle CommonNutritionStyle(){
        Map<Integer, NutritionStyle> common = new HashMap<>();
        common.put(userRepo.UsualNutritionStyle(), NutritionStyle.USUAL);
        common.put(userRepo.VeganNutritionStyle(), NutritionStyle.VEGAN);
        common.put(userRepo.VegeterianNutritionStyle(), NutritionStyle.VEGETARIAN);
        common.put(userRepo.RedutarianNutritionStyle(), NutritionStyle.REDUTARIAN);
        common.put(userRepo.SportNutritionStyle(), NutritionStyle.SPORT);
        int maxValue = Math.max(Math.max(Math.max(userRepo.UsualNutritionStyle(), userRepo.VeganNutritionStyle()),
                Math.max(userRepo.VegeterianNutritionStyle(), userRepo.RedutarianNutritionStyle())), userRepo.SportNutritionStyle());
        return common.get(maxValue);
    }

    public void deleteUserByName(String userName){
        userRepo.deleteByUsername(userName);
    }

    public boolean activateUser(String code) {
        CustomUserDetails customUserDetails = customUserDetailsRepo.findByActivationCode(code);
        if (customUserDetails == null) {
           return false;
        }

        User user = userRepo.findByUserDetails(customUserDetails);
        user.setRole(Collections.singleton(Role.USER));
        user.setActive(true);
        customUserDetails.setActivationCode(null);

        //userRepo.save(user);
        customUserDetailsRepo.save(customUserDetails);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    //    public String CommonContradition(){
//        int SugarCount = userRepo.SugarContradiction();
//        int FryCount = userRepo.FryContradiction();
//        int MeatCount = userRepo.MeatContradiction();
//        int GlutenCount = userRepo.GlutenContradiction();
//        int LactoseCount = userRepo.LactoseContradiction();
//        int HotCount = userRepo.HotContradiction();
//
//        return Integer.toString(Math.max(Math.max(Math.max(SugarCount, FryCount), Math.max(MeatCount, GlutenCount)) ,
//                Math.max(LactoseCount, HotCount)));
//    }

    public CustomUserDetails loadUserByEmail(String email){
        return customUserDetailsRepo.findByEmail(email);
    }
}
