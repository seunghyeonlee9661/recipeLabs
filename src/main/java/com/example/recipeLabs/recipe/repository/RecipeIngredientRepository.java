package com.example.recipeLabs.recipe.repository;
import com.example.recipeLabs.recipe.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
}
