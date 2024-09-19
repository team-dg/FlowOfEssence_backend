package com.lolclone.authentication_management_server.infrastructure;

import com.lolclone.authentication_management_server.application.HttpRequestTokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CompositeHttpRequestTokenExtractor implements HttpRequestTokenExtractor {

    private final List<HttpRequestTokenExtractor> httpRequestTokenExtractors;

    @Override
    public Optional<String> extract(HttpServletRequest request) {
        for(HttpRequestTokenExtractor httpRequestTokenExtractor : httpRequestTokenExtractors) {
            Optional<String> token = httpRequestTokenExtractor.extract(request);
            if(token.isPresent()) {
                return token;
            }
        }
        return Optional.empty();
    }
}
