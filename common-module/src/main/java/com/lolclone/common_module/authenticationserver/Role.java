package com.lolclone.common_module.authenticationserver;

import java.lang.annotation.Annotation;

import com.lolclone.common_module.authenticationserver.annotation.Anonymous;
import com.lolclone.common_module.authenticationserver.annotation.Member;
import com.lolclone.common_module.commonexception.UnexpectedException;

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
            throw new UnexpectedException("해당하는 권한이 없습니다.");
        }
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }
}
