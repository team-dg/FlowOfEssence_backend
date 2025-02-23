package com.lolclone.authenticationmanagementdomain.domain.oauth2;

import java.util.Map;

import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Properties.ProviderProperties;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;
    private final String accessToken;
    private final ProviderProperties providerProps;

    public NaverOAuth2UserInfo(Map<String, Object> attributes, String accessToken, ProviderProperties providerProps) {
        this.attributes = attributes;
        this.accessToken = accessToken;
        this.providerProps = providerProps;
    }

    @Override
    public OAuth2Provider getProvider() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProvider'");
    }

    @Override
    public String getAccessToken() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccessToken'");
    }

    @Override
    public Map<String, Object> getAttributes() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAttributes'");
    }

    @Override
    public String getSocialId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSocialId'");
    }

    @Override
    public String getEmail() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmail'");
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public String getFirstName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFirstName'");
    }

    @Override
    public String getLastName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLastName'");
    }

    @Override
    public String getNickname() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNickname'");
    }

    @Override
    public String getProfileImageUrl() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProfileImageUrl'");
    }
    
}
