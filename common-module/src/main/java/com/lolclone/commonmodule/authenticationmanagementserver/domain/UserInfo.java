package com.lolclone.commonmodule.authenticationmanagementserver.domain;

import lombok.Builder;

@Builder
public record UserInfo(
    String socialId,
    SocialType socialType
) {
    
}
