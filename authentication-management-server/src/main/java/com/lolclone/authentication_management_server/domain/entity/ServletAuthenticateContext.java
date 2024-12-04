package com.lolclone.authentication_management_server.domain.entity;

import com.lolclone.commonmodule.apigatewayserver.domain.AnonymousAuthentication;
import com.lolclone.commonmodule.apigatewayserver.domain.AuthenticateContext;
import com.lolclone.commonmodule.apigatewayserver.domain.Authentication;
import com.lolclone.commonmodule.apigatewayserver.domain.Role;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
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
    public Long getId() {
        return authentication.getId();
    }

    @Override
    public Role getRole() {
        return authentication.getRole();
    }
}
