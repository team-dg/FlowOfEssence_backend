package com.lolclone.authenticationmanagementdomain.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    STARTED("회원가입 시작"),
    CREATING_USER("사용자 생성 중"),
    COMPLETED("회원가입 완료"),
    FAILED("회원가입 실패");

    private String description;   
}
