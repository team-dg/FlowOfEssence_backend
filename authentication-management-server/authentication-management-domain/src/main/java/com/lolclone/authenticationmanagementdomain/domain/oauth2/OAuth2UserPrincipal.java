package com.lolclone.authenticationmanagementdomain.domain.oauth2;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.lolclone.authenticationmanagementdomain.domain.AuthenticatedUser;

import lombok.Getter;

@Getter
public class OAuth2UserPrincipal implements OidcUser, AuthenticatedUser {
    private final UUID memberId;
    private final String email;
    private final OAuth2UserInfo oauth2UserInfo;
    private final OidcUser oidcUser;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String registrationId;

    // OAuth2 로그인
    public OAuth2UserPrincipal(UUID memberId, String email, OAuth2UserInfo oauth2UserInfo, Collection<? extends GrantedAuthority> authorities) {
        this.memberId = memberId;
        this.email = email;
        this.oauth2UserInfo = oauth2UserInfo;
        this.authorities = authorities;
        this.oidcUser = null;
        this.registrationId = oauth2UserInfo.getProvider().getRegistrationId();
    }

    // OIDC 로그인
    public OAuth2UserPrincipal(UUID memberId, String email, OidcUser oidcUser, Collection<? extends GrantedAuthority> authorities, String registrationId) {
        this.memberId = memberId;
        this.email = email;
        this.oidcUser = oidcUser;
        this.authorities = authorities;
        this.oauth2UserInfo = null;
        this.registrationId = registrationId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser != null ? oidcUser.getAttributes() : oauth2UserInfo.getAttributes();
    }

    @Override
    public String getName() {
        return oidcUser != null ? oidcUser.getName() : oauth2UserInfo.getName();
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser != null ? oidcUser.getClaims() : null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser != null ? oidcUser.getUserInfo() : null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser != null ? oidcUser.getIdToken() : null;
    }

    @Override
    public UUID getId() {
        return memberId;
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.fromRegistrationId(registrationId).orElse(OAuth2Provider.DEFAULT);
    }
}
