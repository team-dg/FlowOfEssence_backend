package com.lolclone.common_module.authenticationserver.domain.authentication;

import com.lolclone.common_module.authenticationserver.Role;
import com.lolclone.common_module.commonexception.UnexpectedException;

public class MemberAuthentication implements Authentication{

    private final Long id;

    public MemberAuthentication(Long id) {
        if (id == null) {
            throw new UnexpectedException("id는 null이 될 수 없습니다.");
        }
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Role getRole() {
        return Role.MEMBER;
    }
    
}
