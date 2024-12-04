package com.lolclone.authentication_management_server.domain;


import com.lolclone.authentication_management_server.domain.entity.SocialType;
import com.lolclone.authentication_management_server.domain.entity.UserInfo;

public interface OpenIdClient {
    UserInfo getUserInfo(String idToken);

    SocialType getSocialType();
}
