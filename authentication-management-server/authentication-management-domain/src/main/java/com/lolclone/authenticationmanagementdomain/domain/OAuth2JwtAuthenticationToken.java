package com.lolclone.authenticationmanagementdomain.domain;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class OAuth2JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String token;
    private final Object principal;

    public OAuth2JwtAuthenticationToken(String token, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.principal = principal;
        setAuthenticated(true);
    }

    public OAuth2JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.principal = null;
        setAuthenticated(false);
    }

    
    @Override
    public Object getCredentials() {
        return token;
    }
    @Override
    public Object getPrincipal() {
        return principal;
    }
}
