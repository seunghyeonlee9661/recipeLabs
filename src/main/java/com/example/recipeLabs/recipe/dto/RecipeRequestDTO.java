package com.example.recipeLabs.recipe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RecipeRequestDTO {

    @Schema(description = "레시피 제목", example = "돼지고기 김치찌개")
    private String title; // 레시피 제목

    @Schema(description = "레시피 설명", example = "매운 김치와 돼지고기가 어우러진 맛있는 김치찌개")
    private String description; // 레시피 설명

    @Schema(description = "레시피 인원수", example = "2")
    private Integer servings; // 레시피 인원수

}