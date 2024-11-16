package com.example.recipeLabs.recipe.listener;

import com.example.recipeLabs.notification.service.NotificationService;
import com.example.recipeLabs.recipe.event.RecipeEvent;
import com.example.recipeLabs.user.entity.Follow;
import com.example.recipeLabs.user.entity.User;
import com.example.recipeLabs.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecipeEventListener {
    public final FollowRepository followRepository;
    public final NotificationService notificationService;

    @EventListener
    public void handleNewRecipeEvent(RecipeEvent event) {
        List<User> followers = followRepository.findFollowersByUser(event.getUser());
        for (User follower : followers) {
            String message = event.getUser().getName() + " : 새로운 레시피를 업로드했습니다.";
            notificationService.sendNotification(follower, message);
        }
    }
}