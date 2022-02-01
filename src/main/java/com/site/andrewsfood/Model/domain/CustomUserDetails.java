package com.site.andrewsfood.Model.domain;


import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "usr_details")
public class CustomUserDetails implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Некоректна пошта!")
    @NotBlank(message = "Поле пошти порожнє!")
    private String email;
    private String activationCode;

//    @OneToOne(mappedBy = "user")
//    private User user;

    @NotBlank(message = "Вкажіть стать!")
    private String sex;

    @NotNull(message = "Поле віку порожнє!")
    @Range(min = 16, max = 120)
    private Integer age;
    @NotNull(message = "Поле зросту порожнє!")
    @Range(min = 50, max = 250)
    private Integer height;
    @NotNull(message = "Поле ваги порожнє!")
    @Range(min = 30, max = 250)
    private Integer weight;

  //  @ElementCollection(targetClass = NutritionStyle.class, fetch = FetchType.EAGER)
  //  @Enumerated(EnumType.STRING)
    private String nutritionStyle;

    private String lifeStyle;
    //  @ElementCollection(targetClass = BodyConstitution.class, fetch = FetchType.EAGER)
  //  @Enumerated(EnumType.STRING)
    private String bodyConstitution;

   // @ElementCollection(targetClass = TrainType.class, fetch = FetchType.EAGER)
   // @Enumerated(EnumType.STRING)
    private String trainType;

   // @ElementCollection(targetClass = Religion.class, fetch = FetchType.EAGER)
  //  @Enumerated(EnumType.STRING)
    private String religion;

    @NotNull(message = "Поле калорійності порожнє!")
    @Range(min = 1000, max = 8000)
    private Integer calority;

    @NotNull(message = "Поле білків порожнє!")
    private Integer proteinsCoef;

    @NotNull(message = "Поле жирів порожнє!")
    private Integer lipidsCoef;

    @NotNull(message = "Поле вуглеводів порожнє!")
    private Integer carboCoef;

    @NotNull(message = "Поле цукрів порожнє!")
    private Integer sugarsCoef;

    @ElementCollection(targetClass = Contradictions.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_contradictions", joinColumns = @JoinColumn(name = "user_details_id"))
    @Enumerated(EnumType.STRING)
    Set<Contradictions> contradictions;

    public CustomUserDetails() {
    }

    public CustomUserDetails(String email, String activationCode) {
        this.email = email;
        this.activationCode = activationCode;
    }

    public CustomUserDetails(@Email(message = "Некоректна пошта!") @NotBlank(message = "Поле пошти порожнє!") String email,
                             String activationCode,
                             @NotBlank(message = "Вкажіть стать!") String sex,
                             @NotNull(message = "Поле віку порожнє!") @Range(min = 16, max = 120) Integer age,
                             @NotNull(message = "Поле зросту порожнє!") @Range(min = 50, max = 250) Integer height,
                             @NotNull(message = "Поле ваги порожнє!") @Range(min = 30, max = 250) Integer weight,
                             String nutritionStyle,
                             String lifeStyle,
                             String bodyConstitution,
                             String trainType,
                             String religion,
                             @NotNull(message = "Поле калорійності порожнє!") @Range(min = 1000, max = 8000) Integer calority,
                             @NotNull(message = "Поле білків порожнє!") Integer proteinsCoef,
                             @NotNull(message = "Поле жирів порожнє!") Integer lipidsCoef,
                             @NotNull(message = "Поле вуглеводів порожнє!") Integer carboCoef,
                             @NotNull(message = "Поле цукрів порожнє!") Integer sugarsCoef, Set<Contradictions> contradictions) {
        this.email = email;
        this.activationCode = activationCode;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.nutritionStyle = nutritionStyle;
        this.lifeStyle = lifeStyle;
        this.bodyConstitution = bodyConstitution;
        this.trainType = trainType;
        this.religion = religion;
        this.calority = calority;
        this.proteinsCoef = proteinsCoef;
        this.lipidsCoef = lipidsCoef;
        this.carboCoef = carboCoef;
        this.sugarsCoef = sugarsCoef;
        this.contradictions = contradictions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
//
    public String getNutritionStyle() {
        return nutritionStyle;
    }

    public void setNutritionStyle(String nutritionStyle) {
        this.nutritionStyle = nutritionStyle;
    }
    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public Integer getCalority() {
        return calority;
    }

    public void setCalority(Integer calority) {
        this.calority = calority;
    }

    public String getBodyConstitution() {
        return bodyConstitution;
    }

    public void setBodyConstitution(String bodyConstitution) {
        this.bodyConstitution = bodyConstitution;
    }

    public Set<Contradictions> getContradictions() {
        return contradictions;
    }

    public void setContradictions(Set<Contradictions> contradictions) {
        this.contradictions = contradictions;
    }

    public Integer getProteinsCoef() {
        return proteinsCoef;
    }

    public void setProteinsCoef(Integer proteinsCoef) {
        this.proteinsCoef = proteinsCoef;
    }

    public Integer getLipidsCoef() {
        return lipidsCoef;
    }

    public void setLipidsCoef(Integer lipidsCoef) {
        this.lipidsCoef = lipidsCoef;
    }

    public Integer getCarboCoef() {
        return carboCoef;
    }

    public void setCarboCoef(Integer carboCoef) {
        this.carboCoef = carboCoef;
    }

    public Integer getSugarsCoef() {
        return sugarsCoef;
    }

    public void setSugarsCoef(Integer sugarsCoef) {
        this.sugarsCoef = sugarsCoef;
    }

    public String getLifeStyle() {
        return lifeStyle;
    }

    public void setLifeStyle(String lifeStyle) {
        this.lifeStyle = lifeStyle;
    }
}
