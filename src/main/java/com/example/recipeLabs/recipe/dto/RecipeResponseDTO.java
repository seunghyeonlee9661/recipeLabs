package com.example.recipeLabs.recipe.dto;

import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.user.dto.UserResponseDTO;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<RecipeStepResponseDTO> recipeStepList;
    private int likes;

    public RecipeResponseDTO(Recipe recipe){
        this.id = recipe.getId();
        this.user = new UserResponseDTO(recipe.getUser());
        this.title = recipe.getDescription();
        this.description = recipe.getDescription();
        this.image = recipe.getImage();
        this.ingredients = recipe.getIngredients();
        this.createdAt = recipe.getCreatedAt();
        this.updatedAt = recipe.getUpdatedAt();
        this.recipeStepList = recipe.getRecipesSteps().stream().map(RecipeStepResponseDTO::new).collect(Collectors.toList());
        this.likes = recipe.getLikes();
    }
}
