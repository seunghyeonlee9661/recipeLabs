package com.example.recipeLabs.recipe.entity;
import com.example.recipeLabs.recipe.dto.RecipeRequestDTO;
import com.example.recipeLabs.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;

@Getter
@Entity
@Table(name = "recipes")
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 회원 정보

    @Column(length = 255, nullable = true)
    private String title;

    @Column(length = 255, nullable = true)
    private String description;

    @Column(length = 255, nullable = true)
    private String image;

    @Column(name = "is_complete", nullable = false)
    private Boolean isComplete = false;

    @Column(name = "servings", nullable = true)
    private Integer servings;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false) // 수정 불가
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.REMOVE)
    @OrderBy("stepOrder ASC") // stepOrder 기준으로 오름차순 정렬
    private List<RecipeStep> recipeSteps;

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Like> likes;

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Ingredient> ingredients;

    @ManyToMany
    @JoinTable(name = "recipe_tag",joinColumns = @JoinColumn(name = "recipe_id"),inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags; // 레시피에 달린 태그들

    public Recipe(User user){
        this.user = user;
    }

    public void updateContent(RecipeRequestDTO requestDTO){
        this.title = requestDTO.getTitle();
        this.description = requestDTO.getDescription();
        this.servings = requestDTO.getServings();
    }

    public void updateImage(String image){
        this.image = image;
    }

    // 레시피의 작성이 완료되었음을 체크
    public void setIsComplete(boolean isComplete){
        this.isComplete = isComplete;
    }

    // 레시피 단계 순서 변경 로직
    public void updateStepOrder(int currentOrder, int newOrder) {
        if (currentOrder < newOrder) {
            // 현재 순서가 새 순서보다 앞에 있으면, 중간 단계들 -1
            recipeSteps.stream()
                    .filter(step -> step.getStepOrder() > currentOrder && step.getStepOrder() <= newOrder)
                    .forEach(step -> step.setStepOrder(step.getStepOrder() - 1));
        } else {
            // 현재 순서가 새 순서보다 뒤에 있으면, 중간 단계들 +1
            recipeSteps.stream()
                    .filter(step -> step.getStepOrder() < currentOrder && step.getStepOrder() >= newOrder)
                    .forEach(step -> step.setStepOrder(step.getStepOrder() + 1));
        }
    }

    public OptionalDouble getReviewAverage(){
        return reviews.stream().mapToInt(Review::getRating).average();
    }
}
