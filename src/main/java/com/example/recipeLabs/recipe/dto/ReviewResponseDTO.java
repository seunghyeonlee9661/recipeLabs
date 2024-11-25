package com.example.recipeLabs.recipe.dto;
import com.example.recipeLabs.recipe.entity.Review;
import com.example.recipeLabs.user.dto.UserResponseDTO;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ReviewResponseDTO {
    private final Long id;
    private final String user;
    private final String contents;
    private final LocalDateTime createdAt;
    private final Integer rating;

    public ReviewResponseDTO(Review review){
        this.id = review.getId();
        this.user = review.getUser().getName();
        this.contents = review.getContents();
        this.createdAt = review.getCreatedAt();
        this.rating = review.getRating();
    }
}
