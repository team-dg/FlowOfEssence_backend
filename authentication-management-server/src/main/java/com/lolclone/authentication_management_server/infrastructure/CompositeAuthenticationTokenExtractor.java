package com.lolclone.authentication_management_server.infrastructure;

import com.lolclone.authentication_management_server.domain.AuthenticationTokenExtractor;
import com.lolclone.common_module.authenticationserver.Role;
import com.lolclone.common_module.authenticationserver.domain.authentication.AnonymousAuthentication;
import com.lolclone.common_module.authenticationserver.domain.authentication.Authentication;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeAuthenticationTokenExtractor implements AuthenticationTokenExtractor {

    private final JwtTokenParser jwtTokenParser;
    private final List<AuthenticationClaimsExtractor> authenticationClaimsExtractors;

    @Override
    public Authentication extract(String token) {
        Claims claims = jwtTokenParser.getClaims(token);
        for(AuthenticationClaimsExtractor claimsExtractor : authenticationClaimsExtractors) {
            Authentication authentication = claimsExtractor.extract(claims);
            if(authentication.getRole() != Role.ANONYMOUS) {
                return authentication;
            }
        }
        return AnonymousAuthentication.getInstance();
    }
}
