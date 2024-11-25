package com.example.recipeLabs.user.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

/*
작성자 : 이승현
사용자 로그인 요청 DTO
 */
@Getter
public class UserLoginRequestDTO {

    @NotBlank(message = "아이디를 입력해주세요")
    @Email(message = "유효한 이메일 주소여야 합니다.")
    @Schema(description = "사용자의 이메일(아이디)", example = "user@example.com")
    private String username; // 사용자 이메일(아이디)

    @NotNull(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 8-15자 길이여야 하며, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 포함해야 합니다.")
    @Schema(description = "사용자의 비밀번호", example = "P@ssw0rd123")
    private String password; // 사용자 비밀번호
}