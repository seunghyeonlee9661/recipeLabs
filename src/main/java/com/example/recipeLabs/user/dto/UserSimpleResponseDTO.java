package com.example.recipeLabs.user.dto;

import com.example.recipeLabs.global.enums.Provider;
import com.example.recipeLabs.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserSimpleResponseDTO {

    @Schema(description = "사용자 ID", example = "1", required = true)
    private Long id; // 사용자 ID

    @Schema(description = "사용자 이메일", example = "user@example.com", required = true)
    private String email; // 사용자 이메일


    @Schema(description = "사용자 이름", example = "이승현", required = true)
    private String name; // 사용자

    @Schema(description = "사용자 프로필 이미지 URL", example = "http://example.com/profile.jpg", required = true)
    private String profileImage; // 사용자 프로필 이미지 URL


    public UserSimpleResponseDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.profileImage = user.getProfileImage();
    }
}
