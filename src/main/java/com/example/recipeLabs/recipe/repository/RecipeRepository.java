package com.example.recipeLabs.recipe.repository;
import com.example.recipeLabs.recipe.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findByUserId(Long userId,Pageable pageable);
}
