package com.lolclone.matchmaking_domain.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Champion{
    EZREAL("이즈리얼"),
    GAREN("가렌");

    private final String name;
}