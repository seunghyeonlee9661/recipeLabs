package com.example.recipeLabs.recipe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TagRequestDTO {
    @Schema(description = "레시피 태그 ID")
    private Long id; // 태그 id
}
