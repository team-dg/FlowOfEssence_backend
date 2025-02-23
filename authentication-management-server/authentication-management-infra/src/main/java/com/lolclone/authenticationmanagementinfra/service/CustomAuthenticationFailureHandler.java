package com.lolclone.authenticationmanagementinfra.service;

import static com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType.FAIL_LOGIN;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.AuthenticationFailureHandlingException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.exception.dto.ExceptionResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request, 
        HttpServletResponse response, 
        AuthenticationException exception
    ) throws IOException, ServletException {
        try {
            handleFailureResponse(response);
            final ExceptionResponse errorResponse = new ExceptionResponse(FAIL_LOGIN.getCode(), FAIL_LOGIN.getMessage());
            final String targetUrl = determineTargetUrl(request, errorResponse);
            clearAuthenticationAttributes(request, response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } catch (Exception e) {
            throw new AuthenticationFailureHandlingException(ExceptionType.FAIL_LOGIN);
        }
    }

    private void handleFailureResponse(final HttpServletResponse response) throws IOException {
        response.setStatus(FAIL_LOGIN.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(
            convertToJson(new ExceptionResponse(FAIL_LOGIN.getCode(), FAIL_LOGIN.getMessage())));
    }

    private String convertToJson(final ExceptionResponse errorResponse) throws IOException {
        return objectMapper.writeValueAsString(errorResponse);
    }

    private String determineTargetUrl(HttpServletRequest request, ExceptionResponse errorResponse) {
        final String targetUrl = getTargetUrl(request);
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", errorResponse.code())
                .queryParam("error_description", errorResponse.message())
                .build().toUriString();
    }

    private String getTargetUrl(final HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME);
        return Optional.ofNullable(cookie)
            .map(Cookie::getValue)
            .orElse("/login?error=true");
    }

    protected void clearAuthenticationAttributes(final HttpServletRequest request, final HttpServletResponse response) {
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
