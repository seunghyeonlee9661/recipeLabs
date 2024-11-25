package com.example.recipeLabs.recipe.repository;

import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.recipe.entity.Like;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndRecipe(User user, Recipe recipe); // 레시피에 대한 좋아요 표시
    Optional<Like> findByUserAndRecipe(User user, Recipe recipe); // 레시피에 대한 좋아요 표시
}
