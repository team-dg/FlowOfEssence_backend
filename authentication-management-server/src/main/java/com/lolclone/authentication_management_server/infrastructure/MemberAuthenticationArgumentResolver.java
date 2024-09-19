package com.lolclone.authentication_management_server.infrastructure;

import com.lolclone.common_module.authenticationserver.AuthenticateContext;
import com.lolclone.common_module.authenticationserver.domain.authentication.Authentication;
import com.lolclone.common_module.authenticationserver.domain.authentication.MemberAuthentication;
import com.lolclone.common_module.commonexception.UnexpectedException;
import io.jsonwebtoken.lang.Assert;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberAuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthenticateContext authenticateContext;

    public MemberAuthenticationArgumentResolver(AuthenticateContext authenticateContext) {
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        this.authenticateContext = authenticateContext;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberAuthentication.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = authenticateContext.getAuthentication();
        if(authentication instanceof MemberAuthentication memberAuthentication) {
            return memberAuthentication;
        }
        throw new UnexpectedException("인가된 권한이 인자의 권한과 맞지 않습니다.");
    }
}
