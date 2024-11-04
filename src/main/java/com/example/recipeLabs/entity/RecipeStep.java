package com.example.recipeLabs.entity;

import com.example.recipeLabs.dto.RecipeStepCreateRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "recipes_steps")
@NoArgsConstructor
public class RecipeStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe; // 회원 정보

    @Column(name = "order", nullable = false)
    private Integer order; // 단계 순서

    @Column(name = "content",length = 255, nullable = true)
    private String content; // 단계 내용

    @Column(name = "cooking_time", length = 255, nullable = true)
    private Integer cookingTime; // 소요 시간

    @Column(name = "time", length = 255, nullable = true)
    private String image; // 이미지 URL

    public RecipeStep(RecipeStepCreateRequestDTO requestDTO, Recipe recipe){
        this.recipe = recipe;
        this.order = requestDTO.getOrder();
        this.content = requestDTO.getContent();
        this.cookingTime = requestDTO.getCookingTime();
        this.image = requestDTO.getImage();
    }
}
