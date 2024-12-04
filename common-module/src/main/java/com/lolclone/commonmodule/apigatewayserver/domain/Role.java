package com.lolclone.commonmodule.apigatewayserver.domain;

import com.lolclone.commonmodule.exception.UnexpectedException;
import com.lolclone.commonmodule.apigatewayserver.annotation.Anonymous;
import com.lolclone.commonmodule.apigatewayserver.annotation.Member;
import lombok.Getter;

import java.lang.annotation.Annotation;

@Getter
public enum Role {
    ANONYMOUS(Anonymous.class),
    MEMBER(Member.class),
    ;

    private final Class<? extends Annotation> annotation;

    Role(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public static Role from(String role) {
        try {
            return valueOf(role);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new UnexpectedException("해당하는 권한이 없습니다.", "AUTH-0101");
        }
    }
}
