package com.example.recipeLabs.notification.repository;

import com.example.recipeLabs.notification.entity.Notification;
import com.example.recipeLabs.recipe.entity.Recipe;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUser(User user, Pageable pageable);
    List<Notification> findByUserAndIsReadFalse(User user);
}
