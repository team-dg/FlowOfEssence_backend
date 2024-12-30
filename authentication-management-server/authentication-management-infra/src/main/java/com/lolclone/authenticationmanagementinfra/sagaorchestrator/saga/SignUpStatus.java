package com.lolclone.authenticationmanagementinfra.sagaorchestrator.saga;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SignUpStatus {
    STARTED("회원가입 시작"),
    CREATING_USER("사용자 생성 중"),
    COMPLETED("회원가입 완료"),
    FAILED("회원가입 실패")
    ;
    private String description;
}
