package com.example.recipeLabs.recipe.dto;
import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.recipe.entity.Tag;
import com.example.recipeLabs.user.dto.UserResponseDTO;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;
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

    @Schema(description = "레시피 재료 목록", example = "김치, 돼지고기, 두부, 양파")
    private final List<IngredientResponseDTO> ingredients;

    @Schema(description = "레시피 태그 목록", example = "점심, 술안주, 다이어트")
    private final List<Tag> tags;

    @Schema(description = "레시피 작성 날짜", example = "2024-11-12T10:00:00")
    private final LocalDateTime createdAt;

    @Schema(description = "레시피 단계 목록")
    private final List<RecipeStepResponseDTO> stepList;

    @Schema(description = "레시피 인원수", example = "2")
    private final int servings;

    @Schema(description = "레시피가 사용자의 즐겨찾기 목록에 있는지 여부", example = "true")
    private final boolean isLiked;

    @Schema(description = "레시피 좋아요 수", example = "100")
    private final int likes;

    @Schema(description = "레시피가 사용자의 즐겨찾기 목록에 있는지 여부", example = "false")
    private final boolean isFavorite;

    @Schema(description = "레시피 즐겨찾기 수", example = "50")
    private final int favorites;

    @Schema(description = "레시피 평점 평균", example = "4.3")
    private final OptionalDouble ratingsAverage;

    @Schema(description = "레시피 리뷰 수", example = "26")
    private final int reviews;

    public RecipeResponseDTO(Recipe recipe, boolean isLiked, boolean isFavorite) {
        this.id = recipe.getId(); // 레시피 아이디
        this.user = new UserResponseDTO(recipe.getUser()); // 레시피 작성자
        this.title = recipe.getDescription(); // 레시피 제목
        this.description = recipe.getDescription(); // 레시피 설명
        this.image = recipe.getImage(); // 레시피 이미지
        this.ingredients = recipe.getIngredients().stream().map(IngredientResponseDTO::new).collect(Collectors.toList()); // 레시피 재료 리스트
        this.tags = recipe.getTags(); // 레시피 태그 리스트
        this.createdAt = recipe.getCreatedAt(); // 레시피 작성일자
        this.stepList = recipe.getRecipeSteps().stream().map(RecipeStepResponseDTO::new).collect(Collectors.toList()); // 레시피 단계 목록
        this.reviews = recipe.getReviews().size(); // 레시피 리뷰 개수
        this.likes = recipe.getLikes().size(); // 레시피 좋아요 수
        this.servings = recipe.getServings();
        this.isLiked = isLiked; // 사용자의 레시피 좋아요 여부
        this.isFavorite = isFavorite; // 레시피 즐겨찾기 수
        this.favorites = recipe.getFavorites().size(); // 사용자의 레시피 즐겨찾기 여부
        this.ratingsAverage = recipe.getReviewAverage();
    }
}
