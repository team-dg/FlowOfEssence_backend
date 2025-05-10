package com.lolclone.authenticationmanagementdomain.domain.oauth2;

import java.util.Map;

import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Properties.ProviderProperties;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final String accessToken;
    private final ProviderProperties properties;

    private Map<String, Object> account;
    private Map<String, Object> profile;


    public KakaoOAuth2UserInfo(Map<String, Object> attributes, String accessToken, ProviderProperties properties) {
        this.attributes = attributes;
        this.accessToken = accessToken;
        this.properties = properties;
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.KAKAO;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getSocialId() {
        return String.valueOf(attributes.get(properties.getResponse().getId()));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getAccount() {
        if (account == null) {
            account = (Map<String, Object>) attributes.get(properties.getResponse().getAccount());
        }
        return account;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getProfile() {
        if (profile == null) {
            Map<String, Object> account = getAccount();
            profile = account != null ? 
                (Map<String, Object>) account.get(properties.getResponse().getProfile()) : null;
        }
        return profile;
    }

    // 값을 가져오는 유틸리티 메서드들
    private String getValueFromAccount(String key) {
        Map<String, Object> account = getAccount();
        return account != null ? String.valueOf(account.get(key)) : null;
    }

    private String getValueFromProfile(String key) {
        Map<String, Object> profile = getProfile();
        return profile != null ? String.valueOf(profile.get(key)) : null;
    }

    @Override
    public String getEmail() {
        return getValueFromAccount(properties.getResponse().getEmail());
    }

    @Override
    public String getNickname() {
        return getValueFromProfile(properties.getResponse().getNickname());
    }

    @Override
    public String getProfileImageUrl() {
        return getValueFromProfile(properties.getResponse().getProfileImageUrl());
    }

    @Override
    public String getName() {
        return getNickname();
    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public String getLastName() {
        return null;
    }
}
