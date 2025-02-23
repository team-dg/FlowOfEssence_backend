package com.lolclone.authenticationmanagementdomain.domain;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenClaims {
    private final UUID id;
    private final String email;
    private final OAuth2Provider provider;
    private final List<String> authorities;

    // List<String>을 Collection<? extends GrantedAuthority>로 변환하는 메서드 추가
    public Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        return authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}
