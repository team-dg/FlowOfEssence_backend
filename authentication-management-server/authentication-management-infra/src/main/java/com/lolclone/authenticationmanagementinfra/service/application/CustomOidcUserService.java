package com.lolclone.authenticationmanagementinfra.service.application;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2UserPrincipal;
import com.lolclone.authenticationmanagementinfra.service.domain.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private final MemberService memberService;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            final OidcUser user = super.loadUser(userRequest);
            final String registrationId = userRequest.getClientRegistration().getRegistrationId();
            final String subject = user.getSubject();
            final String socialName = registrationId + "_" + subject;
            final Member member = memberService.findBySocialName(socialName)
                            .map(existingMember -> {
                                existingMember.updateUserInfo(user.getEmail(), user.getPreferredUsername(), user.getPicture());
                                return existingMember;
                            })
                            .orElseGet(() -> 
                                memberService.oAuth2MemberSave(
                                    Member.of(
                                        user.getEmail(),
                                        registrationId,
                                        user.getPreferredUsername(),
                                        user.getPicture()
                                    )
                                )
                            );
            return new OAuth2UserPrincipal(member.getId(), member.getEmail(), user, member.getAuthorities(), registrationId);
        } catch (OAuth2AuthenticationException e) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error(
                    "authentication_error",
                    String.format("사용자 인증에 실패했습니다: %s", e.getMessage()),
                    null
                )
            );
        } catch (Exception e) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error(
                    "server_error",
                    "인증 중 예기치 않은 오류가 발생했습니다",
                    null
                )
            );
        }
    }
}
