package com.lolclone.authenticationmanagementdomain.domain.oauth2;

import com.lolclone.commonmodule.authenticationmanagementserver.domain.SocialType;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.UserInfo;

public interface OAuth2Client {
    UserInfo getUserInfo(String code);

    SocialType getSocialType();
}
