package com.example.recipeLabs.recipe.dto;
import com.example.recipeLabs.recipe.entity.RecipeReview;
import com.example.recipeLabs.user.dto.UserResponseDTO;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class RecipeReviewResponseDTO {
    private final Long id;
    private final UserResponseDTO user;
    private final String contents;
    private final LocalDateTime createdAt;
    private final Integer rating;

    public RecipeReviewResponseDTO(RecipeReview recipeReview){
        this.id = recipeReview.getId();
        this.user = new UserResponseDTO(recipeReview.getUser());
        this.contents = recipeReview.getContents();
        this.createdAt = recipeReview.getCreatedAt();
        this.rating = recipeReview.getRating();
    }
}
