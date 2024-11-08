package com.example.recipeLabs.repository;

import com.example.recipeLabs.entity.User;
import com.example.recipeLabs.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProvider(String email,Provider provider);
    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);
}
