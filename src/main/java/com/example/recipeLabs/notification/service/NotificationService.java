package com.example.recipeLabs.notification.service;

import com.example.recipeLabs.global.security.UserDetailsImpl;
import com.example.recipeLabs.notification.dto.NotificationResposeDTO;
import com.example.recipeLabs.notification.entity.Notification;
import com.example.recipeLabs.notification.repository.NotificationRepository;
import com.example.recipeLabs.user.dto.UserSimpleResponseDTO;
import com.example.recipeLabs.user.entity.User;
import com.example.recipeLabs.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    // 알림 전체 조회 - 페이지네이션
    public ResponseEntity<Page<NotificationResposeDTO>> findNotifications(UserDetailsImpl userDetails, int page){
        Pageable pageable = PageRequest.of(page, 20);
        User user = userDetails.getUser();
        Page<Notification> notificationPage = notificationRepository.findByUser(user, pageable);
        return ResponseEntity.ok(notificationPage.map(NotificationResposeDTO::new));
    }

    // 읽지 않은 알림 조회
    public ResponseEntity<List<NotificationResposeDTO>> findNotificationsUnread(UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        List<Notification> notifications = notificationRepository.findByUserAndIsReadFalse(user);
        return ResponseEntity.ok( notifications.stream().map(NotificationResposeDTO::new).toList());
    }

    public void sendNotification(User user, String message) {
        Notification notification = new Notification(user, message);
        notificationRepository.save(notification);
    }
}
