package com.example.recipeLabs.recipe.entity;
import com.example.recipeLabs.recipe.dto.ReviewRequestDTO;
import com.example.recipeLabs.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "review")
@NoArgsConstructor
public class Review {

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

    public Review(User user, Recipe recipe, ReviewRequestDTO requestDTO){
        this.user = user;
        this.recipe = recipe;
        updateReview(requestDTO);
    }

    public void updateReview(ReviewRequestDTO requestDTO){
        this.contents = requestDTO.getContent();
        this.rating = requestDTO.getRating();
    }
}
