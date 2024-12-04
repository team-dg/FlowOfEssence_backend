package com.lolclone.api_gateway_server.securitys.extractor;

import com.lolclone.commonmodule.apigatewayserver.domain.AnonymousAuthentication;
import com.lolclone.commonmodule.apigatewayserver.domain.Authentication;
import com.lolclone.commonmodule.apigatewayserver.domain.MemberAuthentication;
import com.lolclone.commonmodule.apigatewayserver.domain.Role;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class MemberAuthenticationClaimsExtractor implements AuthenticationClaimsExtractor {
    private static final String MEMBER_ID_KEY = "memberId";

    @Override
    public Mono<Authentication> extract(Claims claims) {
        if (!claims.getAudience().contains(Role.MEMBER.name())) {
            return Mono.just(AnonymousAuthentication.getInstance());
        }
        Long memberId = claims.get(MEMBER_ID_KEY, Long.class);
        return Mono.just(new MemberAuthentication(memberId));
    }
}
