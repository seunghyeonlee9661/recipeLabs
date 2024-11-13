package com.example.recipeLabs.user.dto;

import com.example.recipeLabs.user.entity.User;
import com.example.recipeLabs.global.enums.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDTO {

    @Schema(description = "사용자 ID", example = "1", required = true)
    private Long id; // 사용자 ID

    @Schema(description = "사용자 이메일", example = "user@example.com", required = true)
    private String email; // 사용자 이메일

    @Schema(description = "사용자 제공자 정보", example = "GOOGLE", required = true)
    private Provider provider; // Enum으로 정의된 제공자 (예: GOOGLE, FACEBOOK)

    @Schema(description = "사용자 제공자 ID", example = "google-12345", required = true)
    private String providerId; // 제공자에 의한 고유 ID

    @Schema(description = "사용자 이름", example = "John Doe", required = true)
    private String name; // 사용자 이름

    @Schema(description = "사용자 프로필 이미지 URL", example = "http://example.com/profile.jpg", required = true)
    private String profileImage; // 사용자 프로필 이미지 URL

    @Schema(description = "사용자 생성일", example = "2024-01-01T10:00:00", required = true)
    private LocalDateTime createdAt; // 사용자 생성일

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.provider = user.getProvider();
        this.providerId = user.getProviderId();
        this.name = user.getName();
        this.profileImage = user.getProfileImage();
        this.createdAt = user.getCreatedAt();
    }
}
