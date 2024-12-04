package com.lolclone.commonmodule.apigatewayserver.domain;

import com.lolclone.commonmodule.exception.UnexpectedException;

public class MemberAuthentication implements Authentication {
    private final Long id;

    public MemberAuthentication(Long id) {
        if (id == null) {
            throw new UnexpectedException("id는 null이 될 수 없습니다.", "AUTH-0100");
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
