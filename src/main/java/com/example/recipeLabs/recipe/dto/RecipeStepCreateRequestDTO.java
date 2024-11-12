package com.example.recipeLabs.recipe.dto;

import lombok.Getter;

@Getter
public class RecipeStepCreateRequestDTO {
    private String content; // 단계 내용
    private Integer cookingTime; // 소요 시간
}
