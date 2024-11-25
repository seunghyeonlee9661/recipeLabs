package com.example.recipeLabs.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPasswordUpdateRequestDTO {

    @Schema(description = "현재 비밀번호", example = "currentPassword123!", required = true)
    @NotNull(message = "비밀번호를 입력해주세요")
    private String currentPassword; // 현재 비밀번호

    @Schema(description = "새로운 비밀번호", example = "newPassword123!", required = true)
    @NotNull(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 8-15자 길이여야 하며, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 포함해야 합니다.")
    private String newPassword; // 새로운 비밀번호

    @Schema(description = "새로운 비밀번호 확인", example = "newPassword123!", required = true)
    @NotNull(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 8-15자 길이여야 하며, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 포함해야 합니다.")
    private String newPasswordCheck; // 비밀번호 확인
}
