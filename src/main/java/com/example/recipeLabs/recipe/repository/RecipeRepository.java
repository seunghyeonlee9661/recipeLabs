package com.example.recipeLabs.recipe.repository;
import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findByUser(User user, Pageable pageable);
    Page<Recipe> findAll(Pageable pageable);
    Page<Recipe> findByIsCompleteTrue(Pageable pageable);
}
