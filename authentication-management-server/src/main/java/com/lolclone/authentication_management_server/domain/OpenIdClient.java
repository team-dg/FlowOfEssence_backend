package com.lolclone.authentication_management_server.domain;

import com.lolclone.database_server.authenticationserver.domain.SocialType;
import com.lolclone.database_server.authenticationserver.domain.UserInfo;

public interface OpenIdClient {
    UserInfo getUserInfo(String idToken);

    SocialType getSocialType();
}
