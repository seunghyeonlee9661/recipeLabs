package com.example.recipeLabs.user.dto;
import com.example.recipeLabs.global.enums.Provider;
import lombok.Getter;

@Getter
public class UserOauthCreateRequestDTO {
    private String email;
    private String password;
    private Provider provider;
    private String providerId;
    private String name;
    private String profileImage;
}
