package com.example.recipeLabs.user.entity;
//import com.example.recipeLabs.fridge.entity.FridgeItem;
import com.example.recipeLabs.user.dto.UserCreateRequestDTO;
import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.recipe.entity.Favorite;
import com.example.recipeLabs.recipe.entity.Like;
import com.example.recipeLabs.global.enums.Provider;
import com.example.recipeLabs.user.dto.UserUpdateRequestDTO;
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
    @Column(name = "created_at", updatable = false) // 수정 불가
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
    private List<Like> likeList;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Favorite> favorites;
//
//    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
//    private List<FridgeItem> refrigeratorItemList;

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

    // FIXME : 이후에 사용자 정보 업데이트에 따라 추가로 변경 추가 가능!
    public void updateInfo(UserUpdateRequestDTO requestDTO){
        this.name = requestDTO.getName();
    }

    public void updateImage(String image){
        this.profileImage = image;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    // 사용자 메일 인증
    public void setEmailVerified(){
        this.emailVerified = true;
    }

}