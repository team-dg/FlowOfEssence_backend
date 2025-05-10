package com.lolclone.authenticationmanagementinfra.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolclone.authenticationmanagementdomain.domain.JwtAuthenticationToken;
import com.lolclone.authenticationmanagementdomain.domain.OAuth2JwtAuthenticationToken;
import com.lolclone.authenticationmanagementdomain.domain.TokenClaims;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.InvalidTokenException;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.TokenExpiredException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.exception.dto.ExceptionResponse;
import com.lolclone.authenticationmanagementinfra.service.domain.JwtTokenService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType.TOKEN_EXPIRED_EXCEPTION;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            getToken(request)
                .map(token -> {
                    TokenClaims tokenClaims = jwtTokenService.verifyAndGetClaims(token);
                    if(tokenClaims.getProvider() == OAuth2Provider.DEFAULT) {
                        return new JwtAuthenticationToken(token);
                    }
                    return new OAuth2JwtAuthenticationToken(token);
                })
                .map(jwtAuthenticationToken -> authenticationManager.authenticate(jwtAuthenticationToken))
                .ifPresent(this::setAuthentication);
        } catch (AuthenticationException e) {
            if(e instanceof TokenExpiredException) {
                handleExpiredJwtException(response);
                return;
            }
        } catch (JwtException e) {
            throw new InvalidTokenException(ExceptionType.INVALID_REFRESH_TOKEN);
        }
        filterChain.doFilter(request, response);
    }
    
    private Optional<String> getToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION));
    }

    private void setAuthentication(final Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleExpiredJwtException(final HttpServletResponse response) throws IOException {
        response.setStatus(TOKEN_EXPIRED_EXCEPTION.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter()
                .write(convertToJson(
                        new ExceptionResponse(TOKEN_EXPIRED_EXCEPTION.getCode(), TOKEN_EXPIRED_EXCEPTION.getMessage())));
    }

    private String convertToJson(final ExceptionResponse exceptionResponse) throws IOException {
        return objectMapper.writeValueAsString(exceptionResponse);
    }
}
