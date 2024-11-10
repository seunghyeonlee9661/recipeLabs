package com.example.recipeLabs.recipe.dto;

import lombok.Getter;

@Getter
public class RecipeUpdateRequestDTO {
    private String title;
    private String description;
    private String image;
    private String ingredients;
}
