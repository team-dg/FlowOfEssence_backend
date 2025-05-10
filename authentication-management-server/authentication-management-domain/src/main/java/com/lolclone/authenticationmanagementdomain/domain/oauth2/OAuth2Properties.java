package com.lolclone.authenticationmanagementdomain.domain.oauth2;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "oauth2")
@Component
public class OAuth2Properties {
    private Map<String, ProviderProperties> provider;

    @Getter
    @Setter
    public static class ProviderProperties {
        private String baseUrl;
        private String unlink;
        private String userInfo;
        private String revokeUrl;
        private String tokenUrl;
        private ResponseProperties response;
    }

    @Getter
    @Setter
    public static class ResponseProperties {
        private String account;
        private String profile;
        private String id;
        private String email;
        private String nickname;
        private String profileImageUrl;
        private String resultCode;
        private String message;
        private String response;
        private String name;
    }

    public ProviderProperties getProviderProperties(String providerName) {
        return provider.get(providerName);
    }
}
