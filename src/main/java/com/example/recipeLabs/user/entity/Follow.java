package com.example.recipeLabs.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "follow")
@Getter
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;  // 팔로우한 사용자

    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following;  // 팔로우된 사용자

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;  // 팔로우 생성일

    public Follow(User follower, User following){
        this.follower = follower;
        this.following = following;
    }
}
