package com.example.recipeLabs.recipe.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecipeCreateRequestDTO {
    private String title;
    private String description;
    private String image;
    private String ingredients;
}
