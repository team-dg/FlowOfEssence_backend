package com.lolclone.commonmodule.apigatewayserver.domain;

public interface AuthenticateContext {
    Long getId();
    Role getRole();
    Authentication getAuthentication();
}
