package com.lolclone.authenticationmanagementdomain.domain.openid;

import com.lolclone.commonmodule.authenticationmanagementserver.domain.SocialType;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.UserInfo;

public interface OpenIdClient {
    UserInfo getUserInfo(String idToken);

    SocialType getSocialType();
}
