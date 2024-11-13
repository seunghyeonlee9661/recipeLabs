package com.example.recipeLabs.fridge.repository;

import com.example.recipeLabs.fridge.entity.FridgeItem;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeItemRepository extends JpaRepository<FridgeItem, Long> {
    Page<FridgeItem> findAllByUser(User user, Pageable pageable);
}
