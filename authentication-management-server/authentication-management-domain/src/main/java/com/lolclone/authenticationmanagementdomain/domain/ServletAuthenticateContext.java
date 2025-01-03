package com.lolclone.authenticationmanagementdomain.domain;

import java.util.UUID;

import org.springframework.web.context.annotation.RequestScope;

import com.lolclone.commonmodule.apigatewayserver.domain.AnonymousAuthentication;
import com.lolclone.commonmodule.apigatewayserver.domain.AuthenticateContext;
import com.lolclone.commonmodule.apigatewayserver.domain.Authentication;
import com.lolclone.commonmodule.apigatewayserver.domain.Role;

@RequestScope
public class ServletAuthenticateContext implements AuthenticateContext {
    private Authentication authentication = AnonymousAuthentication.getInstance();

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public UUID getId() {
        return authentication.getId();
    }

    @Override
    public Role getRole() {
        return authentication.getRole();
    }
}
