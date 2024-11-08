package com.example.recipeLabs.global.security;

import com.example.recipeLabs.user.entity.User;
import com.example.recipeLabs.global.enums.Provider;
import com.example.recipeLabs.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/*
작성자 : 이승현
Spring Security : 인증 확인 작업
*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* 인증 확인 작업 */
    public UserDetails loadUserByUsernameAndProvider(String username,String provider) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndProvider(username, Provider.valueOf(provider)).orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
        return new UserDetailsImpl(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}