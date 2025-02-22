package com.lolclone.matchmaking_domain.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Division {
    I(1, "1"),
    II(2, "2"),
    III(3, "3"),
    IV(4, "4");

    private final int value;
    private final String description;
}