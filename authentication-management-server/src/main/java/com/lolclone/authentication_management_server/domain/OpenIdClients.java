package com.lolclone.authentication_management_server.domain;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.lolclone.common_module.commonexception.BadRequestException;
import com.lolclone.common_module.exception.domain.ExceptionType;
import com.lolclone.database_server.authenticationserver.domain.SocialType;

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
            .orElseThrow(() -> new BadRequestException(ExceptionType.OPEN_ID_NOT_SUPPORTED_SOCIAL_TYPE));
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
            throw new BadRequestException(ExceptionType.OAUTH2_DUPLICATE_SOCIAL_TYPE);
        }

        public OpenIdClients build() {
            return new OpenIdClients(openIdClientMap);
        }
    }
}
