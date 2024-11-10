package com.example.recipeLabs.recipe.repository;

import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.recipe.entity.RecipeLike;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long> {
    Page<Recipe> findByUserId(Long userId, Pageable pageable);
    boolean existsByUserAndRecipe(User user, Recipe recipe); // 레시피에 대한 좋아요 표시
}
