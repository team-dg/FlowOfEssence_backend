package com.lolclone.commonmodule.apigatewayserver.annotation;

import com.lolclone.commonmodule.apigatewayserver.domain.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Authorization(role = Role.MEMBER)
public @interface UserAuth {
    
}
