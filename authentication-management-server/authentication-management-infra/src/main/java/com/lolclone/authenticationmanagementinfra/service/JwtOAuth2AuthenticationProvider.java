package com.lolclone.authenticationmanagementinfra.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import com.lolclone.authenticationmanagementdomain.domain.OAuth2JwtAuthenticationToken;
import com.lolclone.authenticationmanagementdomain.domain.TokenClaims;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.InvalidTokenException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.service.domain.JwtTokenService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtOAuth2AuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenService jwtTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try{
            String token = authentication.getPrincipal().toString();
            TokenClaims tokenClaims = jwtTokenService.verifyAndGetClaims(token);
            OidcUser principal = (OidcUser) authentication.getPrincipal();
            return new OAuth2JwtAuthenticationToken(token, principal, tokenClaims.getGrantedAuthorities());
        } catch (JwtException e) {
            throw new InvalidTokenException(ExceptionType.INVALID_REFRESH_TOKEN);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
