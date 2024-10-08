package com.lolclone.database_server.authenticationserver.domain;

import lombok.Builder;

@Builder
public record UserInfo(
    String socialId,
    SocialType socialType,
    String nickname,
    String profileImage
) {
    
}
