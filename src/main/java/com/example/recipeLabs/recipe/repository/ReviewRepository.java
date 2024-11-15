package com.example.recipeLabs.recipe.repository;
import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.recipe.entity.Review;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByIdAndRecipeId(Long reviewId, Long recipeId);
    Page<Review> findAllByRecipeId(Long recipeId, Pageable pageable);
    Boolean existsByRecipeAndUser(Recipe recipe, User user);
}
