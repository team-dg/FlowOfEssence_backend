package com.lolclone.authenticationmanagementinfra.utils;

import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.OAuth2SerializationException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtils {
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
            .path("/")
            .maxAge(maxAge)
            .httpOnly(true)
            .secure(true)
            .sameSite("Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
            .path("/")
            .maxAge(0)
            .httpOnly(true)
            .secure(true)
            .sameSite("Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static String serialize(ObjectMapper objectMapper, OAuth2AuthorizationRequest authorizationRequest) {
        try {
            return Base64.getUrlEncoder()
                .encodeToString(objectMapper.writeValueAsBytes(authorizationRequest));
        } catch (Exception e) {
            throw new OAuth2SerializationException(ExceptionType.OAUTH2_SERIALIZATION_EXCEPTION);
        }
    }

    public static OAuth2AuthorizationRequest deserialize(ObjectMapper objectMapper, Cookie cookie) {
        try {
            return objectMapper.readValue(
                Base64.getUrlDecoder().decode(cookie.getValue()),
                OAuth2AuthorizationRequest.class
            );
        } catch (Exception e) {
            throw new OAuth2SerializationException(ExceptionType.OAUTH2_DESERIALIZATION_EXCEPTION);
        }
    }
}
