package com.lolclone.authenticationmanagementinfra.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.lolclone.authenticationmanagementdomain.domain.CustomUserDetails;
import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2Provider;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.service.domain.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLoginAuthenticationProvider implements AuthenticationProvider {
    private final MemberService memberService;

	// 추후 커스텀 로직 추가할 예정이라 기본 프로바이더 만듬
    @Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName().toString();
		String password = authentication.getCredentials().toString();
        try {
            // 1. 사용자 조회
            Member member = memberService.findMemberByUsername(username)
                .orElseThrow(() -> new BadCredentialsException(ExceptionType.INVALID_CREDENTIALS.getMessage()));

            // 2. 비밀번호 검증
            memberService.verifyPassword(member, password);

            // 3. 인증 성공 처리
            CustomUserDetails userDetails = CustomUserDetails.of(OAuth2Provider.DEFAULT, member);

            // 4. 인증 토큰 반환
            return new UsernamePasswordAuthenticationToken(
                userDetails, 
                null,
                userDetails.getAuthorities()
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(ExceptionType.INVALID_CREDENTIALS.getMessage());
        } catch (Exception e) {
            throw new AuthenticationServiceException(ExceptionType.FAIL_LOGIN.getMessage());
        }
	}

	@Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
