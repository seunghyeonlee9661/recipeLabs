package com.example.recipeLabs.user.dto;

import com.example.recipeLabs.user.entity.User;
import com.example.recipeLabs.global.enums.Provider;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDTO {

    private Long id;
    private String email;
    private Provider provider; // Enum으로 정의
    private String providerId;
    private String name;
    private String profileImage;
    private LocalDateTime createdAt;

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.provider = user.getProvider();
        this.providerId = user.getProviderId();
        this.name = user.getName();
        this.profileImage = user.getProfileImage();
    }
}
