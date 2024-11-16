package com.example.recipeLabs.user.listener;

import com.example.recipeLabs.notification.service.NotificationService;
import com.example.recipeLabs.user.event.FollowEvent;
import com.example.recipeLabs.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowEventListener {
    public final FollowRepository followRepository;
    public final NotificationService notificationService;

    @EventListener
    public void handleNewRecipeEvent(FollowEvent event) {
        String message = event.getTarget().getName() + "님이 팔로우했습니다.";
        notificationService.sendNotification(event.getUser(), message);
    }
}