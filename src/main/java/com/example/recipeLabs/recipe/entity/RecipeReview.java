package com.example.recipeLabs.recipe.entity;
import com.example.recipeLabs.recipe.dto.RecipeReviewRequestDTO;
import com.example.recipeLabs.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "recipe_review")
@NoArgsConstructor
public class RecipeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 회원 정보

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe; // 레시피

    @Lob
    @Column(name = "contents",nullable = true)
    private String contents;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false) // 수정 불가
    private LocalDateTime createdAt;

    @Column(name = "rating", nullable = true)
    private Integer rating;

    public RecipeReview(User user, Recipe recipe, RecipeReviewRequestDTO requestDTO){
        this.user = user;
        this.recipe = recipe;
        updateReview(requestDTO);
    }

    public void updateReview(RecipeReviewRequestDTO requestDTO){
        this.contents = requestDTO.getContent();
        this.rating = requestDTO.getRating();
    }
}
