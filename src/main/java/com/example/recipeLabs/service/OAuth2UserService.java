package com.example.recipeLabs.service;

import com.example.recipeLabs.entity.User;
import com.example.recipeLabs.enums.Provider;
import com.example.recipeLabs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(OAuth2UserService.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 사용자 정보
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 발급자 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId : {}",registrationId);

        Map<String, Object> attributes = extractUserAttributes(registrationId, oAuth2User.getAttributes());
        Provider provider = Provider.valueOf(registrationId.toUpperCase());
        String providerId = attributes.get("providerId").toString();

        // 사용자 계정 확인 후 처리
        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    // 새 사용자 생성
                    User newUser = new User(
                            (String) attributes.get("email"),
                            provider,
                            providerId,
                            (String) attributes.get("name"),
                            (String) attributes.get("profileImage")
                    );
                    log.info("새 회원 - 회원가입");
                    return userRepository.save(newUser);
                });

        // 사용자 정보 SecurityContextHolder 저장
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("SecurityContextHolder 저장");

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        return new DefaultOAuth2User(null, oAuth2User.getAttributes(), userNameAttributeName);
    }

    /**
     * 각 플랫폼의 키에 맞게 속성 값 추출
     */
    private Map<String, Object> extractUserAttributes(String registrationId, Map<String, Object> attributes) {
        Map<String, Object> userAttributes = new HashMap<>();

        switch (registrationId) {
            case "kakao":
                userAttributes.put("providerId", attributes.get("id"));
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                userAttributes.put("email", kakaoAccount.get("email"));
                Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
                userAttributes.put("name", kakaoProfile.get("nickname"));
                userAttributes.put("profileImage", kakaoProfile.get("profile_image_url"));
                break;

            case "naver":
                Map<String, Object> naverResponse = (Map<String, Object>) attributes.get("response");
                userAttributes.put("providerId", naverResponse.get("id"));
                userAttributes.put("email", naverResponse.get("email"));
                userAttributes.put("name", naverResponse.get("name"));
                userAttributes.put("profileImage", naverResponse.get("profile_image"));
                break;

            case "google":
                userAttributes.put("providerId", attributes.get("sub"));
                userAttributes.put("email", attributes.get("email"));
                userAttributes.put("name", attributes.get("name"));
                userAttributes.put("profileImage", attributes.get("picture"));
                break;

            default:
                throw new IllegalArgumentException("Unsupported provider: " + registrationId);
        }
        return userAttributes;
    }
}