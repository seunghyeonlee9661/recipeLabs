package com.example.recipeLabs.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPasswordResetRequestDTO {

    @Schema(description = "리셋 코드", example = "reset1234", required = true)
    @NotNull(message = "리셋 코드는 필수입니다.")
    private String resetCode; // 비밀번호 리셋 코드

    @Schema(description = "새로운 비밀번호", example = "P@ssw0rd123", required = true)
    @NotNull(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 8-15자 길이여야 하며, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 포함해야 합니다.")
    private String newPassword; // 새로운 비밀번호

    @Schema(description = "비밀번호 확인", example = "P@ssw0rd123", required = true)
    @NotNull(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 8-15자 길이여야 하며, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 포함해야 합니다.")
    private String newPasswordCheck; // 비밀번호 확인

}