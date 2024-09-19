package com.lolclone.authentication_management_server.application;

import com.lolclone.database_server.authenticationserver.domain.SocialType;
import com.lolclone.database_server.authenticationserver.domain.UserInfo;

public interface OAuth2Client {
    UserInfo getUserInfo(String code);

    SocialType getSocialType();
}
