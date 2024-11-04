package com.example.recipeLabs.dto;

import lombok.Getter;

@Getter
public class RecipeStepCreateRequestDTO {
    private Integer order; // 단계 순서
    private String content; // 단계 내용
    private String time; // 소요 시간
    private String image; // 이미지 URL
}