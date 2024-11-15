package com.example.recipeLabs.user.repository;

import com.example.recipeLabs.user.entity.Follow;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);  // 팔로우 관계 조회
}
