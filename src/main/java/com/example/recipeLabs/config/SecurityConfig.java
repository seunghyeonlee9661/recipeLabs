package com.example.recipeLabs.config;
import com.example.recipeLabs.filter.JwtAuthenticationFilter;
import com.example.recipeLabs.filter.JwtAuthorizationFilter;
import com.example.recipeLabs.filter.RequestLoggingFilter;
import com.example.recipeLabs.security.JwtUtil;
import com.example.recipeLabs.security.UserDetailsServiceImpl;
import com.example.recipeLabs.service.OAuth2UserService;
import com.example.recipeLabs.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisService redisService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final OAuth2UserService OAuth2UserService;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
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
                                .loginPage("/users/login") // 명시적으로 로그인 페이지 경로를 설정
                                .loginProcessingUrl("/users/login") // 로그인 처리를 /users/login에서 수행
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
                // Oauth2 설정
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/users/login")
                        .successHandler(successHandler())
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(OAuth2UserService))
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // 모든 출처 허용
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용된 메소드
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Oauth2 성공 처리
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        log.info("successHandler 접근!!!");
        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            String id = defaultOAuth2User.getAttributes().get("id").toString();
            String body = """
                    {"id":"%s"}
                    """.formatted(id);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            PrintWriter writer = response.getWriter();
            writer.println(body);
            writer.flush();
        });
    }
}