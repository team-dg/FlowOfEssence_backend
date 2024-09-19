package com.lolclone.authentication_management_server.infrastructure;

import com.lolclone.authentication_management_server.application.HttpRequestTokenExtractor;
import com.lolclone.authentication_management_server.domain.AuthenticationTokenExtractor;
import com.lolclone.common_module.authenticationserver.AuthenticateContext;
import com.lolclone.common_module.authenticationserver.Role;
import com.lolclone.common_module.authenticationserver.domain.authentication.Authentication;
import com.lolclone.common_module.commonexception.ForbiddenException;
import com.lolclone.common_module.commonexception.UnauthorizedException;
import com.lolclone.common_module.exception.domain.ExceptionType;
import io.jsonwebtoken.lang.Assert;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class FixedAuthorizationInterceptor implements HandlerInterceptor {
    private final HttpRequestTokenExtractor httpRequestTokenExtractor;
    private final AuthenticationTokenExtractor authenticationTokenExtractor;
    private final AuthenticateContext authenticateContext;
    private final Role role;

    public FixedAuthorizationInterceptor(
            HttpRequestTokenExtractor httpRequestTokenExtractor,
            AuthenticationTokenExtractor authenticationTokenExtractor,
            AuthenticateContext authenticateContext,
            Role role
    ) {
        Assert.notNull(httpRequestTokenExtractor, "The httpRequestTokenExtractor must not be null");
        Assert.notNull(authenticationTokenExtractor, "The authenticationTokenExtractor must not be null");
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        Assert.notNull(role, "The role must not be null");
        this.httpRequestTokenExtractor = httpRequestTokenExtractor;
        this.authenticationTokenExtractor = authenticationTokenExtractor;
        this.authenticateContext = authenticateContext;
        this.role = role;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = httpRequestTokenExtractor.extract(request)
            .orElseThrow(() -> new UnauthorizedException(ExceptionType.NEED_AUTH_TOKEN));
        Authentication authentication = authenticationTokenExtractor.extract(token);
        if (authentication.getRole() != role) {
            throw new ForbiddenException(ExceptionType.NOT_ENOUGH_PERMISSION);
        }
        authenticateContext.setAuthentication(authentication);
        return true;
    }
}
