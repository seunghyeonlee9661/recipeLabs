package com.example.recipeLabs.user.repository;

import com.example.recipeLabs.user.entity.Follow;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);  // 팔로우 관계 조회


    // 특정 사용자를 팔로우하는 사람들 조회
    @Query("SELECT f.follower FROM Follow f WHERE f.following = :user")
    List<User> findFollowersByUser(User user);

    // 특정 사용자가 팔로우하는 사람들 조회
    @Query("SELECT f.following FROM Follow f WHERE f.follower = :user")
    List<User> findFollowingsByUser(User user);


}
