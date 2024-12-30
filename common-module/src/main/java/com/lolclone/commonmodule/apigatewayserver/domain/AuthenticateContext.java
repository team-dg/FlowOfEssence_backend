package com.lolclone.commonmodule.apigatewayserver.domain;

import java.util.UUID;

public interface AuthenticateContext {
    UUID getId();
    Role getRole();
    Authentication getAuthentication();
}
