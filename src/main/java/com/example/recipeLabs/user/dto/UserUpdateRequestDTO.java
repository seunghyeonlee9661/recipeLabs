package com.example.recipeLabs.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserUpdateRequestDTO {
    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;
}

