package com.example.recipeLabs.recipe.repository;
import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.recipe.entity.RecipeReview;
import com.example.recipeLabs.recipe.entity.RecipeTag;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeReviewRepository extends JpaRepository<RecipeReview, Long> {
    Boolean existsByRecipeAndUser(Recipe recipe, User user);
}
