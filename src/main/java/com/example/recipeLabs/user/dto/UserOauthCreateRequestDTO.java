package com.example.recipeLabs.user.dto;
import com.example.recipeLabs.global.enums.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserOauthCreateRequestDTO {

    @Schema(description = "사용자의 이메일", example = "user@example.com")
    private String email; // 사용자 이메일

    @Schema(description = "사용자의 비밀번호", example = "P@ssw0rd123")
    private String password; // 사용자 비밀번호

    @Schema(description = "사용자 인증 제공자 (예: GOOGLE, FACEBOOK 등)", example = "GOOGLE")
    private Provider provider; // 인증 제공자 (예: GOOGLE, FACEBOOK)

    @Schema(description = "인증 제공자의 고유 ID", example = "12345")
    private String providerId; // 인증 제공자의 고유 ID

    @Schema(description = "사용자 이름", example = "John Doe")
    private String name; // 사용자 이름

    @Schema(description = "사용자 프로필 이미지 URL", example = "http://example.com/profile.jpg")
    private String profileImage; // 사용자 프로필 이미지 URL
}