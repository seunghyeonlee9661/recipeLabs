package com.example.recipeLabs.recipe.dto;

import com.example.recipeLabs.recipe.entity.Recipe;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecipeSimpleResponseDTO {
    private Long id;
    private String user_name;
    private String title;
    private String description;
    private String image;
    private String ingredients;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likes;

    public RecipeSimpleResponseDTO(Recipe recipe){
        this.id = recipe.getId();
        this.user_name = recipe.getUser().getName();
        this.title = recipe.getDescription();
        this.description = recipe.getDescription();
        this.image = recipe.getImage();
        this.ingredients = recipe.getIngredients();
        this.createdAt = recipe.getCreatedAt();
        this.updatedAt = recipe.getUpdatedAt();
        this.likes = recipe.getLikes();
    }
}
