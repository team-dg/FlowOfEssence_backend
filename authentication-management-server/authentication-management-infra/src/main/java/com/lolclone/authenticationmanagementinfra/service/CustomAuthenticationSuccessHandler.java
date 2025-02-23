package com.lolclone.authenticationmanagementinfra.service;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2UserPrincipal;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.TokenGenerationException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.service.domain.JwtTokenService;
import com.lolclone.authenticationmanagementinfra.service.domain.MemberService;
import com.lolclone.authenticationmanagementinfra.service.domain.TokenManagementService;
import com.lolclone.authenticationmanagementserviceapi.dto.TokenRefreshResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import static com.lolclone.authenticationmanagementinfra.service.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final TokenManagementService tokenManagementService;
    private final JwtTokenService jwtTokenService;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
        final HttpServletRequest request, 
        final HttpServletResponse response, 
        final Authentication authentication
    ) throws IOException, ServletException {
        try {
            // 리다이렉트 처리
            String targetUrl = determineTargetUrl(request, response, authentication);
            
            if (response.isCommitted()) {
                logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
                return;
            }
            clearAuthenticationAttributes(request, response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } catch (Exception e) {
            logger.error("Authentication success handling failed", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected String determineTargetUrl(
        final HttpServletRequest request, 
        final HttpServletResponse response, 
        final Authentication authentication
    ) {
        Cookie cookie = WebUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME);
        String targetUrl = Optional.ofNullable(cookie)
            .map(Cookie::getValue)
            .orElse(getDefaultTargetUrl());
        
        final OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        final OAuth2Provider provider = getProvider(principal);

        try {
            final UUID id = principal.getId();
            final Member member = memberService.getOrThrow(id);
            // 토큰 생성 및 저장
            handleTokenGeneration(id, member, provider, response);
        } catch (IOException e) {
            throw new TokenGenerationException(ExceptionType.TOKEN_GENERATION_FAILED);
        }
        return targetUrl;
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(final Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof OAuth2UserPrincipal)) {
            throw new IllegalArgumentException("Unsupported principal type");
        }
        return (OAuth2UserPrincipal) principal;
    }

    private OAuth2Provider getProvider(final OAuth2UserPrincipal principal) {
        if (principal.getUserInfo() != null) {  // OIDC 로그인
            return OAuth2Provider.fromRegistrationId(principal.getRegistrationId()).orElse(OAuth2Provider.DEFAULT);
        }
        return principal.getOauth2UserInfo().getProvider();  // OAuth2 로그인
    }

    private void handleTokenGeneration(UUID userId, Member member, OAuth2Provider provider, HttpServletResponse response) throws IOException {
        final TokenRefreshResponse tokens = jwtTokenService.createTokenResponse(member, provider);
        final String refreshToken = tokens.refreshToken().token();

        // 토큰 저장
        tokenManagementService.saveRefreshToken(userId, refreshToken);
        //TODO: 토큰 저장 후 리다이렉트 처리
        handleSuccessResponse(response, tokens);
    }

    private void handleSuccessResponse(final HttpServletResponse response, final TokenRefreshResponse tokens) throws IOException {
        response.setStatus(OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter()
                .write(convertToJson(tokens));
    }

    private String convertToJson(final TokenRefreshResponse tokens) throws IOException {
        return objectMapper.writeValueAsString(tokens);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
