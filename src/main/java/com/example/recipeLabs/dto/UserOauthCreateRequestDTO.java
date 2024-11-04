package com.example.recipeLabs.dto;
import com.example.recipeLabs.enums.Provider;
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
