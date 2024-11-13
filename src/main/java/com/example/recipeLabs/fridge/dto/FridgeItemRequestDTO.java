package com.example.recipeLabs.fridge.dto;

import com.example.recipeLabs.global.enums.Category;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FridgeItemRequestDTO {
    private String ingredient; // 재료
    private String quantity; // 수량
    private LocalDate expirationDate; // 유통기한
    private Category category; // 카테고리
}
