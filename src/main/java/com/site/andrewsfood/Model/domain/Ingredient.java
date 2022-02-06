package com.site.andrewsfood.Model.domain;

import com.site.andrewsfood.Model.domain.enums.Contradictions;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Введіть назву інгредієнта!")
    private String ingredientName;
    @NotBlank(message = "Категорію не вказано!")
    private String category;

    @NotNull(message = "Вкажіть калорійність!")
    @Range(min = 0, max = 1000)
    private Double calority;

    @NotNull(message = "Вкажість вміст білків!")
    @Range(min = 0, max = 100)
    private Double proteins;

    @NotNull(message = "Вкажість вміст жирів!")
    @Range(min = 0, max = 100)
    private Double lipids;

    @NotNull(message = "Вкажість вміст вуглеводів!")
    @Range(min = 0, max = 100)
    private Double carbo;

    @NotNull(message = "Вкажість вміст цукрів!")
    @Range(min = 0, max = 100)
    private Double sugars;

    private String fileImg;

    @NotNull(message = "Одиниця виміру не вказана!")
    private String unitMeasure;

    @ElementCollection(targetClass = Contradictions.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    Set<Contradictions> ingredientContradictions;

    @Transient
    public String getImagePath() {
    if (fileImg == null || id == null) {return null;}
    else if (fileImg.contains("default")) {return "/src/main/resources/photos/ingredient-photos/default/" + fileImg;}
    else { return "/src/main/resources/photos/ingredient-photos/" + id + "/" + fileImg;}
    }


    public Ingredient() {
    }

    public Ingredient(@NotBlank(message = "Введіть назву інгредієнта!") String ingredientName,
                      @NotBlank(message = "Категорію не вказано!") String category,
                      @Range(min = 0, max = 1000) Double calority,
                      @Range(min = 0, max = 100) Double proteins,
                      @Range(min = 0, max = 100) Double lipids,
                      @Range(min = 0, max = 100) Double carbo,
                      @Range(min = 0, max = 100) Double sugars,
                      String fileImg,
                      @NotNull(message = "Одиниця виміру не вказана!") String unitMeasure,
                      Set<Contradictions> ingredientContradictions) {
        this.ingredientName = ingredientName;
        this.category = category;
        this.calority = calority;
        this.proteins = proteins;
        this.lipids = lipids;
        this.carbo = carbo;
        this.sugars = sugars;
        this.fileImg = fileImg;
        this.unitMeasure = unitMeasure;
        this.ingredientContradictions = ingredientContradictions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Double getCalority() {
        return calority;
    }

    public void setCalority(Double calority) {
        this.calority = calority;
    }

    public Double getProteins() {
        return proteins;
    }

    public void setProteins(Double proteins) {
        this.proteins = proteins;
    }

    public Double getLipids() {
        return lipids;
    }

    public void setLipids(Double lipids) {
        this.lipids = lipids;
    }

    public Double getCarbo() {
        return carbo;
    }

    public void setCarbo(Double carbo) {
        this.carbo = carbo;
    }

    public Double getSugars() {
        return sugars;
    }

    public void setSugars(Double sugars) {
        this.sugars = sugars;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFileImg() {
        return fileImg;
    }

    public void setFileImg(String fileImg) {
        this.fileImg = fileImg;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

    public void setUnitMeasure(String unitMeasure) {
        this.unitMeasure = unitMeasure;
    }

    public Set<Contradictions> getIngredientContradictions() {
        return ingredientContradictions;
    }

    public void setIngredientContradictions(Set<Contradictions> ingredientContradictions) {
        this.ingredientContradictions = ingredientContradictions;
    }

//    public byte[] getFilename() {
//        return filename;
//    }
//
//    public void setFilename(byte[] filename) {
//        this.filename = filename;
//    }
//
//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public String getFileType() {
//        return fileType;
//    }
//
//    public void setFileType(String fileType) {
//        this.fileType = fileType;
//    }
}
