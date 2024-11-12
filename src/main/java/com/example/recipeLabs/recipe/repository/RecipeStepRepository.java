package com.example.recipeLabs.recipe.repository;
import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.recipe.entity.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {
    Optional<RecipeStep> findByRecipeAndStepOrder(Recipe recipe, int stepOrder);
}
