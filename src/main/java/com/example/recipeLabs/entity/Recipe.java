package com.example.recipeLabs.entity;
import com.example.recipeLabs.dto.RecipeCreateRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "recipe")
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 회원 정보

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255, nullable = true)
    private String description;

    @Column(length = 255, nullable = true)
    private String image;

    @Lob // 큰 데이터로 저장
    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "created_at", updatable = false) // 수정 불가
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<RecipeStep> recipesSteps;

    public Recipe(RecipeCreateRequestDTO requestDTO, User user){
        this.user = user;
        this.title = requestDTO.getTitle();
        this.description = requestDTO.getDescription();
        this.image = requestDTO.getImage();
        this.ingredients = requestDTO.getIngredients();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(RecipeCreateRequestDTO requestDTO){
        this.title = requestDTO.getTitle();
        this.description = requestDTO.getDescription();
        this.image = requestDTO.getImage();
        this.ingredients = requestDTO.getIngredients();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
