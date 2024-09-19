package com.lolclone.authentication_management_server.application;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

public interface HttpRequestTokenExtractor {
    Optional<String> extract(HttpServletRequest request);
}
