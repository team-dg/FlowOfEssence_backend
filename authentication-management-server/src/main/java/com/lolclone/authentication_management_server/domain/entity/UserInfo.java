package com.lolclone.authentication_management_server.domain.entity;

import lombok.Builder;

@Builder
public record UserInfo(
    String socialId,
    SocialType socialType,
    String nickname,
    String profileImage
) {
    
}
