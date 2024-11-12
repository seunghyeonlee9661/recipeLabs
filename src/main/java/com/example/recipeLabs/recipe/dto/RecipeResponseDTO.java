package com.example.recipeLabs.recipe.dto;

import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.user.dto.UserResponseDTO;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RecipeResponseDTO {
    private final Long id;
    private final UserResponseDTO user;
    private final String title;
    private final String description;
    private final String image;
    private final String ingredients;
    private final LocalDateTime createdAt;
    private final List<RecipeStepResponseDTO> recipeStepList;
    private final boolean isLiked;
    private final int likes;

    public RecipeResponseDTO(Recipe recipe,boolean isLiked){
        this.id = recipe.getId();
        this.user = new UserResponseDTO(recipe.getUser());
        this.title = recipe.getDescription();
        this.description = recipe.getDescription();
        this.image = recipe.getImage();
        this.ingredients = recipe.getIngredients();
        this.createdAt = recipe.getCreatedAt();
        this.recipeStepList = recipe.getRecipeSteps().stream().map(RecipeStepResponseDTO::new).collect(Collectors.toList());
        this.likes = recipe.getLikes();
        this.isLiked = isLiked;
    }
}
