package com.lolclone.commonmodule.apigatewayserver.annotation;

import com.lolclone.commonmodule.apigatewayserver.domain.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorization {
    Role role();
}
