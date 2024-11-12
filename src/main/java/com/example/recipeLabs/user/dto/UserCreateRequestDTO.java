package com.example.recipeLabs.user.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserCreateRequestDTO {

    @Schema(description = "이메일", example = "userid@email.com")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 주소여야 합니다.")
    private String email;

    @Schema(description = "이름", example = "홍길동")
    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    @Schema(description = "8-15자 길이의 비밀번호로, 최소 하나의 문자, 숫자, 특수 문자를 포함해야 합니다.",example = "Password123!")
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "비밀번호는 8-15자 길이여야 하며, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 포함해야 합니다.")
    private String password;

    @Schema(description = "8-15자 길이의 비밀번호로, 최소 하나의 문자, 숫자, 특수 문자를 포함해야 합니다.",example = "Password123!")
    @NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "비밀번호는 8-15자 길이여야 하며, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 포함해야 합니다.")
    private String passwordCheck;
}