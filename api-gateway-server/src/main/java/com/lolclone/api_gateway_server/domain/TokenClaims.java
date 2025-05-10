package com.lolclone.api_gateway_server.domain;

import java.util.List;
import java.util.UUID;

import com.lolclone.commonmodule.domain.OAuth2Provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenClaims {
    private final UUID id;
    private final String email;
    private final OAuth2Provider provider;
    private final List<String> authorities;
}
