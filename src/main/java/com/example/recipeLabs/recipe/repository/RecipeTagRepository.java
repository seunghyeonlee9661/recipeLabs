package com.example.recipeLabs.recipe.repository;
import com.example.recipeLabs.recipe.entity.RecipeTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeTagRepository extends JpaRepository<RecipeTag, Long> {
    List<RecipeTag> findByNameContaining(String search);
}
