package com.lolclone.authentication_management_server.infrastructure;

import com.lolclone.common_module.authenticationserver.Role;
import com.lolclone.common_module.authenticationserver.domain.authentication.AnonymousAuthentication;
import com.lolclone.common_module.authenticationserver.domain.authentication.Authentication;
import com.lolclone.common_module.authenticationserver.domain.authentication.MemberAuthentication;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthenticationClaimsExtractor implements AuthenticationClaimsExtractor {

    private static final String MEMBER_ID_KEY = "memberId";

    @Override
    public Authentication extract(Claims claims) {
        if (!claims.getAudience().contains(Role.MEMBER.name())) {
            return AnonymousAuthentication.getInstance();
        }
        Long memberId = claims.get(MEMBER_ID_KEY, Long.class);
        return new MemberAuthentication(memberId);
    }
}

