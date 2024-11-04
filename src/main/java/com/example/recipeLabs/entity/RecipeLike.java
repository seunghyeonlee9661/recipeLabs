package com.example.recipeLabs.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "recipes_likes")
@NoArgsConstructor // 기본 생성자 추가
public class RecipeLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 유저

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe; // 레시피

    public RecipeLike(User user, Recipe recipe) {
        this.user = user;
        this.recipe = recipe;
    }
}
