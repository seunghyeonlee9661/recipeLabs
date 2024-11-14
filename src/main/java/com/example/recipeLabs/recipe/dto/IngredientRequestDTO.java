package com.example.recipeLabs.recipe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class IngredientRequestDTO {

    @Schema(description = "재료 이름", example = "돼지고기")
    private String name;

    @Schema(description = "재료 량", example = "100g")
    private String quantity;
}
