package com.example.recipeLabs.recipe.dto;

import com.example.recipeLabs.recipe.entity.Ingredient;
import lombok.Getter;

@Getter
public class IngredientResponseDTO {
    private final Long id;
    private final String name;
    private final String quantity;

    public IngredientResponseDTO(Ingredient ingredient) {
        this.id = ingredient.getId();
        this.name = ingredient.getName();
        this.quantity = ingredient.getQuantity();
    }
}
