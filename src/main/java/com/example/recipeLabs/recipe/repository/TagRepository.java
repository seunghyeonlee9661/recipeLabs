package com.example.recipeLabs.recipe.repository;
import com.example.recipeLabs.recipe.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByNameContaining(String search);
}
