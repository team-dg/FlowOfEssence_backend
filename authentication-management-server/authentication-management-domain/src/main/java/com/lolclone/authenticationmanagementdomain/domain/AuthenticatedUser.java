package com.lolclone.authenticationmanagementdomain.domain;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;

public interface AuthenticatedUser {
    UUID getId();
    String getEmail();
    OAuth2Provider getProvider();
    Collection<? extends GrantedAuthority> getAuthorities();
}
