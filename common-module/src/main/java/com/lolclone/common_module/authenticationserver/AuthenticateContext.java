package com.lolclone.common_module.authenticationserver;

import com.lolclone.common_module.authenticationserver.domain.authentication.AnonymousAuthentication;
import com.lolclone.common_module.authenticationserver.domain.authentication.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class AuthenticateContext {
    private Authentication authentication = AnonymousAuthentication.getInstance();

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Long getId() {
        return authentication.getId();
    }

    public Role getRole(){
        return authentication.getRole();
    }

    public Authentication getAuthentication(){
        return authentication;
    }
}
