package com.lolclone.authenticationmanagementinfra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolclone.authenticationmanagementinfra.service.CustomAccessDeniedHandler;
import com.lolclone.authenticationmanagementinfra.service.CustomAuthenticationEntryPoint;
import com.lolclone.authenticationmanagementinfra.service.CustomAuthenticationFailureHandler;
import com.lolclone.authenticationmanagementinfra.service.CustomAuthenticationSuccessHandler;
import com.lolclone.authenticationmanagementinfra.service.CustomLoginAuthenticationProvider;
import com.lolclone.authenticationmanagementinfra.service.HttpCookieOAuth2AuthorizationRequestRepository;
import com.lolclone.authenticationmanagementinfra.service.JwtAuthenticationFilter;
import com.lolclone.authenticationmanagementinfra.service.JwtLoginAuthenticationProvider;
import com.lolclone.authenticationmanagementinfra.service.JwtOAuth2AuthenticationProvider;
import com.lolclone.authenticationmanagementinfra.service.application.CustomJdbcOAuth2AuthorizedClientService;
import com.lolclone.authenticationmanagementinfra.service.application.CustomOAuth2UserService;
import com.lolclone.authenticationmanagementinfra.service.application.CustomOidcUserService;
import com.lolclone.authenticationmanagementinfra.service.domain.JwtTokenService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private final CustomJdbcOAuth2AuthorizedClientService customJdbcOAuth2AuthorizedClientService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;

    private final ObjectMapper objectMapper;
    private final JwtTokenService jwtTokenService;

    private final CustomLoginAuthenticationProvider customLoginAuthenticationProvider;
    private final JwtOAuth2AuthenticationProvider jwtOAuth2AuthenticationProvider;
    private final JwtLoginAuthenticationProvider jwtLoginAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        AuthenticationManager authenticationManager
    ) throws Exception {
        return http
            .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 비활성화 -> JWT 기반 인증, 보안성이 낮은 Basic 인증 제외
            .csrf(AbstractHttpConfigurer::disable) // 기본 CSRF 보호 비활성화 -> REST API는 stateless 하므로 필요 x
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .rememberMe(AbstractHttpConfigurer::disable) // 기본 리멤버미 비활성화 -> 로그인 상태 장기 유지 필요 x refresh token 사용
            .requestCache(RequestCacheConfigurer::disable) // 기본 요청 캐시 비활성화 -> 인증 전 요청을 저장했다가 인증 후 원래 페이지로 리다이렉트 보틍은 클라이언트에서 라우팅 처리
            .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Jwt 기반 인증이므로 세션 비활성화
            .securityContext(AbstractHttpConfigurer::disable)

            .exceptionHandling((exception) -> 
                exception.authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
            )

            .addFilterBefore(new JwtAuthenticationFilter(objectMapper, authenticationManager, jwtTokenService), UsernamePasswordAuthenticationFilter.class)

            .oauth2Login((oauth2) -> oauth2
                .authorizationEndpoint(config -> config.authorizationRequestRepository(
                    httpCookieOAuth2AuthorizationRequestRepository
                ))
                .authorizedClientService(customJdbcOAuth2AuthorizedClientService)
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                    .oidcUserService(customOidcUserService)
                )
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
            )

            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
            )

            .build();
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_USER > ROLE_ANONYMOUS");
        return roleHierarchy;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http
            .getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(customLoginAuthenticationProvider)
            .authenticationProvider(jwtOAuth2AuthenticationProvider)
            .authenticationProvider(jwtLoginAuthenticationProvider)
            .build();
    }
}
