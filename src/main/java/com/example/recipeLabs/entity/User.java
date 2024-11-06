package com.example.recipeLabs.entity;
import com.example.recipeLabs.dto.UserCreateRequestDTO;
import com.example.recipeLabs.dto.UserOauthCreateRequestDTO;
import com.example.recipeLabs.enums.Provider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략
    private Long id;

    @Column(length = 255, nullable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Provider provider; // Enum으로 정의

    @Column(length = 255, nullable = true)
    private String providerId;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(name = "profile_image", length = 255, nullable = true)
    private String profileImage;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 이메일 인증 여부 필드
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    // 인증 코드 필드
    @Column(name = "email_verification_code", length = 255, nullable = true)
    private String emailVerificationCode;  

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<RecipeLike> recipeLikeList;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<RecipeFavorite> recipeFavorites;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<RefrigeratorItem> refrigeratorItemList;

    // 기본 회원가입
    public User(UserCreateRequestDTO requetDTO,String password, String code){
        this.email = requetDTO.getEmail();
        this.password = password;
        this.provider = Provider.LOCAL;
        this.name = requetDTO.getName();
        this.emailVerificationCode = code;
    }

    // Oauth 유저 생성
    public User(String email, Provider provider, String providerId, String name, String profileImage) {
        this.email = email;
        this.password =  UUID.randomUUID().toString();
        this.provider = provider;
        this.providerId = providerId;
        this.name = name;
        this.profileImage = profileImage;
        this.emailVerified = true;
        this.emailVerificationCode = "";
    }

    // 사용자 메일 인증
    public void setEmailVerified(){
        this.emailVerified = true;
    }

}