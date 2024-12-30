package com.lolclone.commonmodule.apigatewayserver.domain;

import java.util.UUID;

import com.lolclone.commonmodule.exception.UnexpectedException;

public class MemberAuthentication implements Authentication {
    private final UUID id;

    public MemberAuthentication(UUID id) {
        if (id == null) {
            throw new UnexpectedException("id는 null이 될 수 없습니다.", "AUTH-0100");
        }
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Role getRole() {
        return Role.MEMBER;
    }
}
