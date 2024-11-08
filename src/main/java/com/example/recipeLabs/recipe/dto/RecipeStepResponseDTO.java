package com.example.recipeLabs.recipe.dto;
import com.example.recipeLabs.recipe.entity.RecipeStep;

public class RecipeStepResponseDTO {
    private Long id;
    private Integer stepOrder; // 단계 순서
    private String content; // 단계 내용
    private Integer cookingTime; // 소요 시간
    private String image; // 이미지 URL

    public RecipeStepResponseDTO(RecipeStep recipeStep){
        this.id = recipeStep.getId();
        this.stepOrder = recipeStep.getStepOrder();
        this.content = recipeStep.getContent();
        this.cookingTime = recipeStep.getCookingTime();
        this.image = recipeStep.getImage();
    }
}
