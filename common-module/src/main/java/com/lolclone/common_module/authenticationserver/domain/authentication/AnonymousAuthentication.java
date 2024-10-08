package com.lolclone.common_module.authenticationserver.domain.authentication;

import com.lolclone.common_module.authenticationserver.Role;

public class AnonymousAuthentication implements Authentication {

    private static final AnonymousAuthentication INSTANCE = new AnonymousAuthentication();

    private AnonymousAuthentication() {
    }

    public static Authentication getInstance() {
        return INSTANCE;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public Role getRole() {
        return Role.ANONYMOUS;
    }
}
