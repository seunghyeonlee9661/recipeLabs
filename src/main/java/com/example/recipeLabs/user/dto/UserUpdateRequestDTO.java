package com.example.recipeLabs.user.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateRequestDTO {

    @NotBlank(message = "이름은 필수 항목입니다.")
    @Schema(description = "사용자 이름", example = "이승현", required = true)
    private String name; // 사용자 이름
    
    @Schema(description = "사용자 소개글", example = "요리를 좋아하는 자취생입니다.", required = false)
    private String introduction; // 소개
}