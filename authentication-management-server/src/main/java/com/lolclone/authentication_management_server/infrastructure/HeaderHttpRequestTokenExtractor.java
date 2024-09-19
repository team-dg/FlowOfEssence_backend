package com.lolclone.authentication_management_server.infrastructure;

import com.lolclone.authentication_management_server.application.HttpRequestTokenExtractor;
import com.lolclone.common_module.commonexception.UnauthorizedException;
import com.lolclone.common_module.exception.domain.ExceptionType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

public class HeaderHttpRequestTokenExtractor implements HttpRequestTokenExtractor {

    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    @Override
    public Optional<String> extract(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return Optional.empty();
        }
        return Optional.of(extractToken(header));
    }

    private String extractToken(String header) {
        validateHeader(header);
        return header.substring(BEARER_TOKEN_PREFIX.length()).trim();
    }

    private void validateHeader(String header) {
        if (!header.toLowerCase().startsWith(BEARER_TOKEN_PREFIX.toLowerCase())) {
            throw new UnauthorizedException(ExceptionType.NOT_BEARER_TOKEN_TYPE);
        }
    }
}
