package com.lolclone.api_gateway_server.securitys.extractor;


import com.lolclone.api_gateway_server.securitys.jwt.JwtTokenParser;
import com.lolclone.commonmodule.apigatewayserver.domain.AnonymousAuthentication;
import com.lolclone.commonmodule.apigatewayserver.domain.Authentication;
import com.lolclone.commonmodule.apigatewayserver.domain.Role;
import io.jsonwebtoken.Claims;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Primary
@Component
@RequiredArgsConstructor
public class CompositeAuthenticationTokenExtractor implements AuthenticationTokenExtractor {
    private final JwtTokenParser jwtTokenParser;
    private final List<AuthenticationClaimsExtractor> authenticationClaimsExtractors;

    @Override
    public Mono<Authentication> extract(String token) {
        Claims claims = (Claims) jwtTokenParser.getClaims(token);
        return Flux.fromIterable(authenticationClaimsExtractors)
                .flatMap(extractor -> extractor.extract(claims))
                .filter(authentication -> authentication.getRole() != Role.ANONYMOUS)
                .next()
                .defaultIfEmpty(AnonymousAuthentication.getInstance());
    }
}
