package com.lolclone.commonmodule.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OAuth2Provider {
    GOOGLE("google"),
    KAKAO("kakao"),
    NAVER("naver"),
    DEFAULT("default");

    private final String registrationId;
    private static final Map<String, OAuth2Provider> PROVIDER_MAP;

    static {
        PROVIDER_MAP = Collections.unmodifiableMap(Arrays.stream(OAuth2Provider.values())
            .collect(Collectors.toMap(OAuth2Provider::getRegistrationId, Function.identity())));
    }

    public static Optional<OAuth2Provider> fromRegistrationId(final String registrationId) {
        return Optional.ofNullable(PROVIDER_MAP.get(registrationId));
    }
}
