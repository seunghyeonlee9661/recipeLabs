package com.example.recipeLabs.recipe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "tag")
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name; // 태그 이름 (예: "한식", "조식", "매운")

    // 생성자 추가 (필요시)
    public Tag(String name) {
        this.name = name;
    }
}
