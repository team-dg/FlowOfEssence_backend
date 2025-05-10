package com.lolclone.authenticationmanagementdomain.domain;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails, AuthenticatedUser {
    private final OAuth2Provider provider;
    private final UUID memberId;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    public static CustomUserDetails of(OAuth2Provider provider, Member member) {
        return new CustomUserDetails(provider, member.getId(), member.getEmail(), member.getAuthorities());
    }

    @Override
    public UUID getId() {
        return memberId;
    }
}
