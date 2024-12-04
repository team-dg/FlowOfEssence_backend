package com.lolclone.authentication_management_server.infrastructure;

import com.lolclone.authentication_management_server.dto.TokenResponse;
import com.lolclone.commonmodule.apigatewayserver.domain.MemberAuthentication;
import com.lolclone.commonmodule.apigatewayserver.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationTokenProvider {

    private static final String MEMBER_ID_KEY = "memberId";
    private static final long EXPIRATION_MINUTES = 60L * 6L;

    private final TokenProviderTemplate tokenProviderTemplate;

    public TokenResponse provide(MemberAuthentication memberAuthentication) {
        return tokenProviderTemplate.provide(EXPIRATION_MINUTES,
                jwtBuilder -> jwtBuilder
                        .subject(memberAuthentication.getId().toString())
                        .claim(MEMBER_ID_KEY, memberAuthentication.getId())
                        .audience().add(Role.MEMBER.name()).and()
        );
    }
}
