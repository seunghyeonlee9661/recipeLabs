package com.example.recipeLabs.recipe.dto;

import com.example.recipeLabs.recipe.entity.RecipeIngredient;
import lombok.Getter;

@Getter
public class RecipeIngredientResponseDTO {
    private final Long id;
    private final String name;
    private final String quantity;

    public RecipeIngredientResponseDTO(RecipeIngredient recipeIngredient) {
        this.id = recipeIngredient.getId();
        this.name = recipeIngredient.getName();
        this.quantity = recipeIngredient.getQuantity();
    }
}
