package com.example.recipeLabs.recipe.dto;
import com.example.recipeLabs.recipe.entity.RecipeStep;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RecipeStepResponseDTO {

    @Schema(description = "레시피 단계 ID", example = "1")
    private Long id;

    @Schema(description = "단계 순서", example = "1")
    private Integer stepOrder; // 단계 순서

    @Schema(description = "단계 내용", example = "먼저 냄비에 물을 붓고 끓입니다.")
    private String content; // 단계 내용

    @Schema(description = "요리 소요 시간 (분 단위)", example = "10")
    private Integer cookingTime; // 소요 시간

    @Schema(description = "단계 이미지 URL", example = "https://example.com/images/step1.jpg")
    private String image; // 이미지 URL

    public RecipeStepResponseDTO(RecipeStep recipeStep){
        this.id = recipeStep.getId();
        this.stepOrder = recipeStep.getStepOrder();
        this.content = recipeStep.getContent();
        this.cookingTime = recipeStep.getCookingTime();
        this.image = recipeStep.getImage();
    }
}