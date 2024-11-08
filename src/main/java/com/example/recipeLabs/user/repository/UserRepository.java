package com.example.recipeLabs.user.repository;

import com.example.recipeLabs.global.enums.Provider;
import com.example.recipeLabs.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProvider(String email, Provider provider);
    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);
}
