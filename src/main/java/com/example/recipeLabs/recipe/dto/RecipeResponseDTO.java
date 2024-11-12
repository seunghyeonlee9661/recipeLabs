package com.example.recipeLabs.recipe.dto;

import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.user.dto.UserResponseDTO;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
public class RecipeResponseDTO {

    @Schema(description = "레시피 ID", example = "1")
    private final Long id;

    @Schema(description = "레시피 작성자 정보", implementation = UserResponseDTO.class)
    private final UserResponseDTO user;

    @Schema(description = "레시피 제목", example = "맛있는 김치찌개")
    private final String title;

    @Schema(description = "레시피 설명", example = "이 레시피는 한국 전통 김치찌개 만드는 방법입니다.")
    private final String description;

    @Schema(description = "레시피 이미지 URL", example = "http://example.com/recipe-image.jpg")
    private final String image;

    @Schema(description = "레시피 재료", example = "김치, 돼지고기, 두부, 양파")
    private final String ingredients;

    @Schema(description = "레시피 작성 날짜", example = "2024-11-12T10:00:00")
    private final LocalDateTime createdAt;

    @Schema(description = "레시피 단계 목록")
    private final List<RecipeStepResponseDTO> recipeStepList;

    @Schema(description = "레시피가 사용자의 즐겨찾기 목록에 있는지 여부", example = "true")
    private final boolean isLiked;

    @Schema(description = "레시피 좋아요 수", example = "100")
    private final int likes;

    @Schema(description = "레시피가 사용자의 즐겨찾기 목록에 있는지 여부", example = "false")
    private final boolean isFavorite;

    @Schema(description = "레시피 즐겨찾기 수", example = "50")
    private final int favorites;

    public RecipeResponseDTO(Recipe recipe, boolean isLiked, boolean isFavorite) {
        this.id = recipe.getId();
        this.user = new UserResponseDTO(recipe.getUser());
        this.title = recipe.getDescription();
        this.description = recipe.getDescription();
        this.image = recipe.getImage();
        this.ingredients = recipe.getIngredients();
        this.createdAt = recipe.getCreatedAt();
        this.recipeStepList = recipe.getRecipeSteps().stream().map(RecipeStepResponseDTO::new).collect(Collectors.toList());
        this.likes = recipe.getRecipeLikes().size();
        this.isLiked = isLiked;
        this.isFavorite = isFavorite;
        this.favorites = recipe.getRecipeFavorites().size();
    }
}
