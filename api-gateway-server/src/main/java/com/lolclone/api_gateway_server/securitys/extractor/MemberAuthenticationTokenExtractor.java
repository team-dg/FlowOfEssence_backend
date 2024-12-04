package com.lolclone.api_gateway_server.securitys.extractor;

import com.lolclone.api_gateway_server.securitys.jwt.JwtTokenParser;
import com.lolclone.commonmodule.apigatewayserver.domain.Authentication;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("memberAuthenticationTokenExtractor")
@RequiredArgsConstructor
public class MemberAuthenticationTokenExtractor implements AuthenticationTokenExtractor {
    private final JwtTokenParser jwtTokenParser;
    private final MemberAuthenticationClaimsExtractor memberAuthenticationClaimsExtractor;

    @Override
    public Mono<Authentication> extract(String token) {
        Claims claims = (Claims) jwtTokenParser.getClaims(token);
        return memberAuthenticationClaimsExtractor.extract(claims);
    }
}
