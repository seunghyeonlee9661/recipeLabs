package com.example.recipeLabs.notification.entity;

import com.example.recipeLabs.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 알림을 받을 사용자

    @Column(nullable = false)
    private String message; // 알림 내용

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false; // 읽음 여부

    // 생성일자
    @CreationTimestamp
    @Column(name = "created_at", updatable = false) // 수정 불가
    private LocalDateTime createdAt;

    public Notification(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
