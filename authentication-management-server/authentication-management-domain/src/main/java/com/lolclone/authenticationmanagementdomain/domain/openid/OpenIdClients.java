package com.lolclone.authenticationmanagementdomain.domain.openid;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.lolclone.authenticationmanagementdomain.exception.ExceptionType;
import com.lolclone.authenticationmanagementdomain.exception.OpenIdClientException;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.SocialType;

import lombok.extern.slf4j.Slf4j;

public class OpenIdClients {
    private final Map<SocialType, OpenIdClient> openIdClientMap;

    private OpenIdClients(Map<SocialType, OpenIdClient> openIdClientMap) {
        this.openIdClientMap = openIdClientMap;
    }

    public static OpenIdClientsBuilder builder() {
        return new OpenIdClientsBuilder();
    }

    public OpenIdClient getClient(SocialType socialType) {
        return Optional.ofNullable(openIdClientMap.get(socialType))
            .orElseThrow(() -> new OpenIdClientException(ExceptionType.OPEN_ID_NOT_SUPPORTED_SOCIAL_TYPE));
    }
    
    @Slf4j
    public static class OpenIdClientsBuilder {
        private final Map<SocialType, OpenIdClient> openIdClientMap = new EnumMap<>(SocialType.class);

        private OpenIdClientsBuilder() {}

        public OpenIdClientsBuilder addAll(List<OpenIdClient> openIdClients) {
            for(OpenIdClient openIdClient : openIdClients) {
                add(openIdClient);
            }
            return this;
        }

        public OpenIdClientsBuilder add(OpenIdClient openIdClient) {
            SocialType socialType = openIdClient.getSocialType();
            if(openIdClientMap.containsKey(socialType)) {
                logDuplicateSocialType(socialType);
            }
            openIdClientMap.put(socialType, openIdClient);
            return this;
        }

        private void logDuplicateSocialType(SocialType socialType) {
            log.error("[SOCIAL TYPE DUPLICATE] socialType: {} 가 이미 존재합니다.", socialType);
            throw new OpenIdClientException(ExceptionType.OPEN_ID_DUPLICATE_SOCIAL_TYPE);
        }

        public OpenIdClients build() {
            return new OpenIdClients(openIdClientMap);
        }
    }
}
