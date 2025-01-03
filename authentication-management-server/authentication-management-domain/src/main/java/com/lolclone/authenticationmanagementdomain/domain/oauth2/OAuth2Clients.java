package com.lolclone.authenticationmanagementdomain.domain.oauth2;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.lolclone.authenticationmanagementdomain.exception.ExceptionType;
import com.lolclone.authenticationmanagementdomain.exception.OAuth2ClientException;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.SocialType;

import lombok.extern.slf4j.Slf4j;

public class OAuth2Clients {
    private final Map<SocialType, OAuth2Client> oAuth2ClientMap;

    private OAuth2Clients(Map<SocialType, OAuth2Client> oAuth2ClientMap) {
        this.oAuth2ClientMap = oAuth2ClientMap;
    }

    public static OAuth2ClientsBuilder builder() {
        return new OAuth2ClientsBuilder();
    }

    public OAuth2Client getClient(SocialType socialType) {
        return Optional.ofNullable(oAuth2ClientMap.get(socialType))
            .orElseThrow(() -> new OAuth2ClientException(ExceptionType.OAUTH2_NOT_SUPPORTED_SOCIAL_TYPE));
    }

    @Slf4j
    public static class OAuth2ClientsBuilder {
        private final Map<SocialType, OAuth2Client> oAuth2ClientMap = new EnumMap<>(SocialType.class);

        private OAuth2ClientsBuilder() {
        }

        public OAuth2ClientsBuilder addAll(List<OAuth2Client> oAuth2Clients) {
            for (OAuth2Client oAuth2Client : oAuth2Clients) {
                add(oAuth2Client);
            }
            return this;
        }

        public OAuth2ClientsBuilder add(OAuth2Client oAuth2Client) {
            SocialType socialType = oAuth2Client.getSocialType();
            if (oAuth2ClientMap.containsKey(socialType)) {
                logDuplicateSocialType(socialType);
            }
            oAuth2ClientMap.put(socialType, oAuth2Client);
            return this;
        }

        private void logDuplicateSocialType(SocialType socialType) {
            log.error("[SOCIAL TYPE DUPLICATE] socialType: {} 가 이미 존재합니다.", socialType);
            throw new OAuth2ClientException(ExceptionType.OAUTH2_DUPLICATE_SOCIAL_TYPE);
        }

        public OAuth2Clients build() {
            return new OAuth2Clients(oAuth2ClientMap);
        }
    }
}
