package com.lolclone.authentication_management_server.infrastructure;

import com.lolclone.common_module.authenticationserver.domain.authentication.Authentication;
import io.jsonwebtoken.Claims;

public interface AuthenticationClaimsExtractor {
    Authentication extract(Claims claims);
}
