package com.example.recipeLabs.user.repository;

import com.example.recipeLabs.global.enums.Provider;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProvider(String email, Provider provider);
    Optional<User> findByName(String name);
    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);
    Page<User> findByFollowers(User user, Pageable pageable); // 내가 팔로우 한 사람에 대한 조회
    Page<User> findByFollowings(User user, Pageable pageable); // 나를 팔로우 한 사람에 대한 조회
}
