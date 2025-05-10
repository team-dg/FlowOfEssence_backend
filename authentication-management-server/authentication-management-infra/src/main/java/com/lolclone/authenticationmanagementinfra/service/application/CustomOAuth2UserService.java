package com.lolclone.authenticationmanagementinfra.service.application;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2UserInfo;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2UserPrincipal;
import com.lolclone.authenticationmanagementinfra.service.domain.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;
    private final OAuth2UserInfoService oauth2UserInfoService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            final OAuth2User user = super.loadUser(userRequest);
            final String registrationId = userRequest.getClientRegistration().getRegistrationId();
            final String accessToken = userRequest.getAccessToken().getTokenValue();
            final Map<String, Object> attributes = user.getAttributes();
            final OAuth2UserInfo userInfo = oauth2UserInfoService.getOAuth2UserInfo(registrationId, attributes, accessToken);
            final String socialName = userInfo.getProvider().getRegistrationId() + "_" + userInfo.getSocialId();
            final Member member = memberService.findBySocialName(socialName)
                        .map(existingMember -> {
                            existingMember.updateUserInfo(userInfo.getEmail(), userInfo.getNickname(), userInfo.getProfileImageUrl());
                            return existingMember;
                        })
                        .orElseGet(() -> 
                            memberService.oAuth2MemberSave(
                                Member.of(
                                    userInfo.getEmail(),
                                    socialName,
                                    userInfo.getNickname(),
                                    userInfo.getProfileImageUrl()
                                )
                            )
                        );    
            
            return new OAuth2UserPrincipal(member.getId(), member.getEmail(), userInfo, member.getAuthorities());
        } catch (OAuth2AuthenticationException e) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error(
                    "authentication_error",
                    "사용자 인증에 실패했습니다: " + e.getMessage(),
                    null
                )
            );
        } catch (Exception e) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error(
                    "server_error",
                    "인증 중 예기치 않은 오류가 발생했습니다.",
                    null
                )
            );
        }
    }
}
