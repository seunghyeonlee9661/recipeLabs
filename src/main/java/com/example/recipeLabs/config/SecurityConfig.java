package com.example.recipeLabs.config;
import com.example.recipeLabs.filter.JwtAuthenticationFilter;
import com.example.recipeLabs.filter.JwtAuthorizationFilter;
import com.example.recipeLabs.filter.RequestLoggingFilter;
import com.example.recipeLabs.security.JwtUtil;
import com.example.recipeLabs.security.UserDetailsServiceImpl;
import com.example.recipeLabs.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@AllArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisService redisService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /* 로그인과 JWT 생성을 위한 필터 */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    /* JWT 검증 필터 */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    /* 보안 필터의 범위 설정 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS 설정
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 관리 설정
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 접근 권한 설정
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                                .requestMatchers("/error").permitAll() // 오류
//                                .requestMatchers("/login").permitAll() // 로그인
//                                .requestMatchers("/swagger-ui/**").permitAll() //Swagger
//                                .requestMatchers("/v3/api-docs/**").permitAll() //Swagger
//                                .anyRequest().authenticated()
                                .anyRequest().permitAll() // 모든 요청 허용
                )
                // 에러 핸들러 설정
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint))
                // 로그인 처리 설정
                .formLogin(formLogin ->
                        formLogin
                                .loginProcessingUrl("/users/login")
                                .permitAll()
                )
                // 로그아웃 처리 설정
                .logout(logout ->
                        logout
                                .logoutUrl("/users/logout")
                                .addLogoutHandler(this::handleLogout)
                                .logoutSuccessHandler(this::handleLogoutSuccess)
                                .permitAll()
                )
                .addFilterBefore(new RequestLoggingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    /* 패스워드 인코딩 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ObjectMapper - 카카오 로그인에 활용
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessToken = jwtUtil.getTokenFromRequest(request, JwtUtil.AUTHORIZATION_HEADER);
        if (accessToken != null && jwtUtil.validateToken(jwtUtil.substringToken(accessToken))) {
            redisService.delete(RedisService.REFRESH_TOKEN_PREFIX,jwtUtil.substringToken(accessToken));
        }// 쿠키 클리어
        jwtUtil.removeJwtCookie(response);
    }

    private void handleLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Logout successful");
    }

// 4번 시도
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("https://weatherwearclothing.com","https://www.weatherwearclothing.com", "http://localhost:5173")); // 명시적 출처 설정
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용된 메소드
//        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더 허용
//        configuration.setAllowCredentials(true); // 자격 증명 허용
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}