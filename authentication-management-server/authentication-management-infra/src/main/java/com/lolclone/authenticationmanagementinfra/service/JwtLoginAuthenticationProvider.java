package com.lolclone.authenticationmanagementinfra.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.lolclone.authenticationmanagementdomain.domain.CustomUserDetails;
import com.lolclone.authenticationmanagementdomain.domain.JwtAuthenticationToken;
import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.authenticationmanagementdomain.domain.TokenClaims;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.InvalidTokenException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.service.domain.JwtTokenService;
import com.lolclone.authenticationmanagementinfra.service.domain.MemberService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtLoginAuthenticationProvider implements AuthenticationProvider {
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String token = authentication.getPrincipal().toString();
            TokenClaims tokenClaims = jwtTokenService.verifyAndGetClaims(token);
            Member member = memberService.getOrThrow(tokenClaims.getId());
            UserDetails userDetails = new CustomUserDetails(tokenClaims.getProvider(), member.getId(), member.getEmail(), member.getAuthorities());
            return new JwtAuthenticationToken(token, userDetails, userDetails.getAuthorities());
        } catch (JwtException e) {
            throw new InvalidTokenException(ExceptionType.INVALID_REFRESH_TOKEN);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
