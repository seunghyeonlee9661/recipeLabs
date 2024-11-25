package com.example.recipeLabs.recipe.repository;
import com.example.recipeLabs.recipe.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
