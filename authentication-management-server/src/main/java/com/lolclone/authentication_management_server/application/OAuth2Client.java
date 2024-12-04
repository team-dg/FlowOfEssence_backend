package com.lolclone.authentication_management_server.application;

import com.lolclone.authentication_management_server.domain.entity.SocialType;
import com.lolclone.authentication_management_server.domain.entity.UserInfo;

public interface OAuth2Client {
    UserInfo getUserInfo(String code);

    SocialType getSocialType();
}
