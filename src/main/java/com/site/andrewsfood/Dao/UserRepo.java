package com.site.andrewsfood.Dao;

import com.site.andrewsfood.Model.domain.CustomUserDetails;
import com.site.andrewsfood.Model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findById(long id);
    User findByUserDetails(CustomUserDetails customUserDetails);

    @Transactional
    void deleteByUsername(String userName);
    @Query("select u from User u where u.role='USER'")
    List<User> findAllByRoleUser();
    @Query("select count(u.user_id) from User u where u.role='USER'")
    Long countAllByUserRole();
    @Query("select count(u.user_id) from User u")
    Long countAllById();
    @Query("select avg(ud.age) from CustomUserDetails ud")
    Integer AvgAge();
    @Query("select avg(ud.calority) from CustomUserDetails ud")
    Integer AvgCalority();
    @Query("select avg(ud.height) from CustomUserDetails ud")
    Integer AvgHeight();
    @Query("select avg(ud.weight) from CustomUserDetails ud")
    Integer AvgWeight();

    @Query("select count(ud.bodyConstitution) from CustomUserDetails ud where ud.bodyConstitution='default'")
    Integer DefaultBodyConstitution();
    @Query("select count(ud.bodyConstitution) from CustomUserDetails ud where ud.bodyConstitution='ecto'")
    Integer EctoBodyConstitution();
    @Query("select count(ud.bodyConstitution) from CustomUserDetails ud where ud.bodyConstitution='mezo'")
    Integer MezoBodyConstitution();
    @Query("select count(ud.bodyConstitution) from CustomUserDetails ud where ud.bodyConstitution='endo'")
    Integer EndoBodyConstitution();

    @Query("select count(ud.nutritionStyle) from CustomUserDetails ud where ud.bodyConstitution='usual'")
    Integer UsualNutritionStyle();
    @Query("select count(ud.nutritionStyle) from CustomUserDetails ud where ud.bodyConstitution='vegan'")
    Integer VeganNutritionStyle();
    @Query("select count(ud.nutritionStyle) from CustomUserDetails ud where ud.bodyConstitution='vegeterian'")
    Integer VegeterianNutritionStyle();
    @Query("select count(ud.nutritionStyle) from CustomUserDetails ud where ud.bodyConstitution='redutarian'")
    Integer RedutarianNutritionStyle();
    @Query("select count(ud.nutritionStyle) from CustomUserDetails ud where ud.bodyConstitution='sport'")
    Integer SportNutritionStyle();

//    @Query("select count(ud.contradictions) from CustomUserDetails ud where ud.contradictions='NOSUGAR'")
//    Integer SugarContradiction();
//    @Query("select count(ud.contradictions) from CustomUserDetails ud where ud.contradictions='NOFRY'")
//    Integer FryContradiction();
//    @Query("select count(ud.contradictions) from CustomUserDetails ud where ud.contradictions='NOMEAT'")
//    Integer MeatContradiction();
//    @Query("select count(ud.contradictions) from CustomUserDetails ud where ud.contradictions='NOGLUTEN'")
//    Integer GlutenContradiction();
//    @Query("select count(ud.contradictions) from CustomUserDetails ud where ud.contradictions='NOLACTOSE'")
//    Integer LactoseContradiction();
//    @Query("select count(ud.contradictions) from CustomUserDetails ud where ud.contradictions='NOHOT'")
//    Integer HotContradiction();

}
