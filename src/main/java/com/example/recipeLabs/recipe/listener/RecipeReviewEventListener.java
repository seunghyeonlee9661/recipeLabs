package com.example.recipeLabs.recipe.listener;

import com.example.recipeLabs.notification.service.NotificationService;
import com.example.recipeLabs.recipe.event.RecipeReviewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecipeReviewEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleRecipeReviewEvent(RecipeReviewEvent event) {
        String message = "새로운 리뷰가 레시피에 추가되었습니다.";
        notificationService.sendNotification(event.getUser(), message);
    }
}