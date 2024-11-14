package com.example.recipeLabs.recipe.dto;

import com.example.recipeLabs.recipe.entity.Recipe;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class RecipeSimpleResponseDTO {

    @Schema(description = "레시피 ID", example = "1")
    private final Long id;

    @Schema(description = "레시피 작성자 이름", example = "홍길동")
    private final String user_name;

    @Schema(description = "레시피 제목", example = "맛있는 김치찌개")
    private final String title;

    @Schema(description = "레시피 설명", example = "이 레시피는 한국 전통 김치찌개 만드는 방법입니다.")
    private final String description;

    @Schema(description = "레시피 이미지 URL", example = "http://example.com/recipe-image.jpg")
    private final String image;

    @Schema(description = "레시피 작성 날짜", example = "2024-11-12T10:00:00")
    private final LocalDateTime createdAt;

    @Schema(description = "레시피 좋아요 수", example = "100")
    private final Integer likes;

    @Schema(description = "레시피 즐겨찾기 수", example = "50")
    private final Integer favorites;

    public RecipeSimpleResponseDTO(Recipe recipe) {
        this.id = recipe.getId();
        this.user_name = recipe.getUser().getName();
        this.title = recipe.getDescription();
        this.description = recipe.getDescription();
        this.image = recipe.getImage();
        this.createdAt = recipe.getCreatedAt();
        this.likes = recipe.getRecipeLikes().size();
        this.favorites = recipe.getRecipeFavorites().size();
    }
}
