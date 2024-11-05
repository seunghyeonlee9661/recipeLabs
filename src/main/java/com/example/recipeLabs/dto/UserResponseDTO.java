package com.example.recipeLabs.dto;

import com.example.recipeLabs.entity.Recipe;
import com.example.recipeLabs.entity.User;
import com.example.recipeLabs.enums.Provider;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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
