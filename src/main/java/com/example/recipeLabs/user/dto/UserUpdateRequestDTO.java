package com.example.recipeLabs.user.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateRequestDTO {

    @NotBlank(message = "이름은 필수 항목입니다.")
    @Schema(description = "사용자 이름", example = "John Doe", required = true)
    private String name; // 사용자 이름
}