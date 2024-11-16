package com.example.recipeLabs.notification.dto;
import com.example.recipeLabs.notification.entity.Notification;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResposeDTO {
    private Long id;
    private String message; // 알림 내용
    private boolean isRead = false; // 읽음 여부
    private LocalDateTime createdAt; // 알림 생성 시간

    public NotificationResposeDTO(Notification notification){
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.isRead = notification.isRead();
        this.createdAt = notification.getCreatedAt();
    }
}
