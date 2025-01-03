package com.lolclone.commonmodule.apigatewayserver.domain;

import java.util.UUID;

public interface Authentication {
    UUID getId();

    Role getRole();
}
