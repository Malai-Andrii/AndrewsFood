package com.site.andrewsfood.Model.domain;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "dish")
public class Dish {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Введіть назву інгредієнта!")
    private String dishName;
    @Transient
    @Range(min = 0, max = 20)
    private Integer cookTimeHour;
    @Transient
    @Range(min = 0, max = 60)
    private Integer cookTimeMinute;

    private String cookTime;
    @NotBlank(message = "Поле способу проиготування порожнє!")
    @Column(length = 4096)
    private String cookMethod;
    private String videoLink;
    @Range(min = 0, max = 100)
    private Double dishLitre;

    private Double dishMass;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ingredient_list", joinColumns = @JoinColumn(name = "ingredient_id"))
    @MapKeyColumn(name = "ingredient_name")
    @Column(name = "amount")
    private Map<String, Double> ingredientList;

    @ElementCollection(targetClass = Contradictions.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    Set<Contradictions> dishContradictions;

    private String dishType;

    private String fileImg;

    private Double dishCalority;
    private Double dishProteins;
    private Double dishLipids;
    private Double dishCarbo;
    private Double dishSugars;

    @Transient
    public String getImagePath() {
        if (fileImg == null || id == null) return null;
        return "/src/main/resources/photos/dish-photos/" + id + "/" + fileImg;
    }

    public Dish() {
    }

    public Dish(@NotBlank(message = "Введіть назву інгредієнта!") String dishName,
                String cookTime,
                @NotBlank(message = "Поле способу проиготування порожнє!") String cookMethod,
                String videoLink,
                @Range(min = 0, max = 100) Double dishLitre,
                Double dishMass,
                Map<String, Double> ingredientList,
                Set<Contradictions> dishContradictions,
                String dishType,
                String fileImg,
                Double dishCalority,
                Double dishProteins,
                Double dishLipids,
                Double dishCarbo,
                Double dishSugars) {
        this.dishName = dishName;
        this.cookTime = cookTime;
        this.cookMethod = cookMethod;
        this.videoLink = videoLink;
        this.dishLitre = dishLitre;
        this.dishMass = dishMass;
        this.ingredientList = ingredientList;
        this.dishContradictions = dishContradictions;
        this.dishType = dishType;
        this.fileImg = fileImg;
        this.dishCalority = dishCalority;
        this.dishProteins = dishProteins;
        this.dishLipids = dishLipids;
        this.dishCarbo = dishCarbo;
        this.dishSugars = dishSugars;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public Integer getCookTimeHour() {
        return cookTimeHour;
    }

    public void setCookTimeHour(Integer cookTimeHour) {
        this.cookTimeHour = cookTimeHour;
    }

    public Integer getCookTimeMinute() {
        return cookTimeMinute;
    }

    public void setCookTimeMinute(Integer cookTimeMinute) {
        this.cookTimeMinute = cookTimeMinute;
    }

    public String getCookMethod() {
        return cookMethod;
    }

    public void setCookMethod(String cookMethod) {
        this.cookMethod = cookMethod;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public Double getDishLitre() {
        return dishLitre;
    }

    public void setDishLitre(Double dishLitre) {
        this.dishLitre = dishLitre;
    }

    public Map<String, Double> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(Map<String, Double> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public String getDishType() {
        return dishType;
    }

    public void setDishType(String dishType) {
        this.dishType = dishType;
    }

    public String getFileImg() {
        return fileImg;
    }

    public void setFileImg(String fileImg) {
        this.fileImg = fileImg;
    }

    public Double getDishCalority() {
        return dishCalority;
    }

    public void setDishCalority(Double dishCalority) {
        this.dishCalority = dishCalority;
    }

    public Double getDishProteins() {
        return dishProteins;
    }

    public void setDishProteins(Double dishProteins) {
        this.dishProteins = dishProteins;
    }

    public Double getDishLipids() {
        return dishLipids;
    }

    public void setDishLipids(Double dishLipids) {
        this.dishLipids = dishLipids;
    }

    public Double getDishCarbo() {
        return dishCarbo;
    }

    public void setDishCarbo(Double dishCarbo) {
        this.dishCarbo = dishCarbo;
    }

    public Double getDishSugars() {
        return dishSugars;
    }

    public void setDishSugars(Double dishSugars) {
        this.dishSugars = dishSugars;
    }

    public Set<Contradictions> getDishContradictions() {
        return dishContradictions;
    }

    public void setDishContradictions(Set<Contradictions> dishContradictions) {
        this.dishContradictions = dishContradictions;
    }

    public Double getDishMass() {
        return dishMass;
    }

    public void setDishMass(Double dishMass) {
        this.dishMass = dishMass;
    }
}
