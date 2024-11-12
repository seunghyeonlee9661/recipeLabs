package com.example.recipeLabs.recipe.repository;

import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.recipe.entity.RecipeFavorite;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeFavoriteRepository extends JpaRepository<RecipeFavorite, Long> {
    Page<RecipeFavorite> findByUser(User user, Pageable pageable);
    boolean existsByUserAndRecipe(User user, Recipe recipe); // 레시피에 대한 좋아요 표시
    Optional<RecipeFavorite> findByUserAndRecipe(User user, Recipe recipe); // 레시피에 대한 좋아요 표시
}
