package com.example.recipeLabs.recipe.repository;
import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.recipe.entity.Review;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Boolean existsByRecipeAndUser(Recipe recipe, User user);
}
