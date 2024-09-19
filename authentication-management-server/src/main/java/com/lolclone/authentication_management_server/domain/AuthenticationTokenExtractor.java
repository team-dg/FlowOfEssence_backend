package com.lolclone.authentication_management_server.domain;

import com.lolclone.common_module.authenticationserver.domain.authentication.Authentication;

public interface AuthenticationTokenExtractor {
    Authentication extract(String token);
}
