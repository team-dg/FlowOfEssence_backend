package com.lolclone.common_module.authenticationserver.domain.authentication;

import com.lolclone.common_module.authenticationserver.Role;

public interface Authentication {
    Long getId();

    Role getRole();
}
