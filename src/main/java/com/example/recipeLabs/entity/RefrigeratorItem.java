package com.example.recipeLabs.entity;
import com.example.recipeLabs.enums.Category;
import jakarta.persistence.*;

@Entity
@Table(name = "refrigerator_items")
public class RefrigeratorItem {

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
    private String expirationDate; // 유통기한

    @Enumerated(EnumType.STRING)
    private Category category; // 카테고리
}
