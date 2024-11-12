package com.example.recipeLabs.recipe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RecipeStepCreateRequestDTO {

    @Schema(description = "레시피 단계 내용", example = "먼저 냄비에 물을 붓고 끓입니다.")
    private String content; // 단계 내용

    @Schema(description = "요리 소요 시간 (분 단위)", example = "10")
    private Integer cookingTime; // 소요 시간
}
