package com.example.recipeLabs.recipe.entity;

import com.example.recipeLabs.recipe.dto.IngredientRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredient")
@Getter
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private String quantity;

    public Ingredient(Recipe recipe, IngredientRequestDTO requestDTO){
        this.recipe = recipe;
        this.name = requestDTO.getName();
        this.quantity = requestDTO.getQuantity();
    }
}
