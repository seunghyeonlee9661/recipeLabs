package com.example.recipeLabs.recipe.dto;

import lombok.Getter;

@Getter
public class RecipeStepCreateRequestDTO {
    private Integer stepOrder; // 단계 순서
    private String content; // 단계 내용
    private Integer cookingTime; // 소요 시간
    private String image; // 이미지 URL
}
