package com.example.recipeLabs.fridge.entity;
import com.example.recipeLabs.fridge.dto.FridgeItemRequestDTO;
import com.example.recipeLabs.global.enums.Category;
import com.example.recipeLabs.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "fridge_items")
@NoArgsConstructor
public class FridgeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 유저

    @Column(length = 255, nullable = false)
    private String ingredient; // 재료

    @Column(length = 255)
    private String quantity; // 수량

    @Column(name = "expiration_date")
    private LocalDate expirationDate; // 유통기한

    @Enumerated(EnumType.STRING)
    private Category category; // 카테고리

    public FridgeItem(FridgeItemRequestDTO requestDTO, User user){
        this.user = user;
        this.ingredient = requestDTO.getIngredient();
        this.quantity = requestDTO.getQuantity();
        this.expirationDate = requestDTO.getExpirationDate();
    }
}