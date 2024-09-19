package com.lolclone.authentication_management_server.infrastructure;

import com.lolclone.authentication_management_server.domain.AuthenticationTokenExtractor;
import com.lolclone.common_module.authenticationserver.domain.authentication.Authentication;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationTokenExtractor implements AuthenticationTokenExtractor {

    private final JwtTokenParser jwtTokenParser;
    private final MemberAuthenticationClaimsExtractor memberAuthenticationClaimsExtractor;

    @Override
    public Authentication extract(String token) {
        Claims claims = jwtTokenParser.getClaims(token);
        return memberAuthenticationClaimsExtractor.extract(claims);
    }
}
