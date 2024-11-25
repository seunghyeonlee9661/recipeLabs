package com.example.recipeLabs.recipe.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ReviewRequestDTO {

    @Schema(description = "레시피 리뷰 내용", example = "양념이 조금 짠거 같아요.")
    private String content; // 단계 내용

    @Schema(description = "레시피 평점", example = "5")
    private Integer rating; // 소요 시간
}
