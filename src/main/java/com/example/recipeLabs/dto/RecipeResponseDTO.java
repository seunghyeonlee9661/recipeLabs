package com.example.recipeLabs.dto;

import com.example.recipeLabs.entity.Recipe;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class RecipeResponseDTO {
    private Long id;
    private UserResponseDTO user;
    private String title;
    private String description;
    private String image;
    private String ingredients;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RecipeResponseDTO(Recipe recipe){
        this.id = recipe.getId();
        this.user = new UserResponseDTO(recipe.getUser());
        this.title = recipe.getDescription();
        this.description = recipe.getDescription();
        this.image = recipe.getImage();
        this.ingredients = recipe.getIngredients();
        this.createdAt = recipe.getCreatedAt();
        this.updatedAt = recipe.getUpdatedAt();
    }
}
