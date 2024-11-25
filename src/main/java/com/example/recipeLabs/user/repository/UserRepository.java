package com.example.recipeLabs.user.repository;

import com.example.recipeLabs.global.enums.Provider;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProvider(String email, Provider provider);
    Optional<User> findByName(String name);
    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);

    @Query("SELECT f.following FROM Follow f WHERE f.follower = :user")
    Page<User> findByFollowers(@Param("user") User user, Pageable pageable);

    @Query("SELECT f.follower FROM Follow f WHERE f.following = :user")
    Page<User> findByFollowings(@Param("user") User user, Pageable pageable);
}
