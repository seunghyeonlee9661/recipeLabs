package com.example.recipeLabs.fridge.dto;

import com.example.recipeLabs.fridge.entity.FridgeItem;
import com.example.recipeLabs.global.enums.Category;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FridgeItemResponseDTO {
    private Long id;
    private String ingredient; // 재료
    private String quantity; // 수량
    private LocalDate expirationDate; // 유통기한
    private Category category; // 카테고리

    public FridgeItemResponseDTO(FridgeItem fridgeItem) {
        this.id = fridgeItem.getId();
        this.ingredient = fridgeItem.getIngredient();
        this.quantity = fridgeItem.getQuantity();
        this.expirationDate = fridgeItem.getExpirationDate();
        this.category = fridgeItem.getCategory();
    }
}
