package com.lolclone.authentication_management_server.domain;

import com.lolclone.database_server.authenticationserver.domain.Member;
import com.lolclone.database_server.authenticationserver.domain.RegistrationType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.lolclone.database_server.authenticationserver.domain.UserInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserInfoMapper {
    private final DefaultNicknamePolicy defaultNicknamePolicy;

    public Member toUser(UserInfo userInfo) {
        String nickname = userInfo.nickname();
        return new Member(
            userInfo.socialId(),
            userInfo.socialType(),
            StringUtils.hasText(nickname) ? nickname : defaultNicknamePolicy.generate(),
            userInfo.profileImage(),
            RegistrationType.SOCIAL
        );
    }
}
