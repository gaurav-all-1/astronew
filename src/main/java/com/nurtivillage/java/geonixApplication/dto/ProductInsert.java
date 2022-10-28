package com.nurtivillage.java.geonixApplication.dto;



import javax.validation.constraints.NotBlank;


import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

import com.amazonaws.services.support.model.Category;

public class ProductInsert {


    @NotBlank(message = "name is required")


    private String name;
    @NotNull(message = "brand is required")
    private String brand;
    @NotNull(message = "category is required")
    private Category category;
    @NotNull(message = "status is required")
    private String status; 
    private String image;

    public ProductInsert(){}

    public ProductInsert(String name,String brand,Category category,String status,String image){
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.image = image;
        this.status = status;
    }
    public String getBrand() {
        return brand;
    }
    public Category getCategory() {
        return category;
    }
    public String getImage() {
        return image;
    }
    public String getName() {
        return name;
    }
    public String getStatus() {
        return status;
    }
}
