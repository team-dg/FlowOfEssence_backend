package com.lolclone.authenticationmanagementinfra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lolclone.authenticationmanagementdomain.domain.AuthenticatedUser;
import com.lolclone.authenticationmanagementdomain.domain.CustomUserDetails;
import com.lolclone.authenticationmanagementdomain.domain.oauth2.OAuth2UserPrincipal;
import com.lolclone.authenticationmanagementinfra.service.application.MemberRegisterFacade;
import com.lolclone.authenticationmanagementinfra.service.application.UserAuthService;
import com.lolclone.authenticationmanagementserviceapi.dto.LoginRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.RefreshTokenRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.SignUpRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.TokenRefreshResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthController {
    private final UserAuthService userAuthService;
    private final MemberRegisterFacade memberRegisterFacade;

    @PostMapping("/login")
    public ResponseEntity<TokenRefreshResponse> login(
        @Valid @RequestBody final LoginRequest loginRequest
    ) {
        final TokenRefreshResponse tokenRefreshResponse = userAuthService.originalLogin(loginRequest);
        return ResponseEntity.ok().body(tokenRefreshResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<TokenRefreshResponse> signUp(
        @Valid @RequestBody final SignUpRequest signUpRequest
    ) {
        final TokenRefreshResponse tokenRefreshResponse = memberRegisterFacade.registerMemberAcrossServices(signUpRequest);
        return ResponseEntity.ok().body(tokenRefreshResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @AuthenticationPrincipal final OAuth2UserPrincipal oauth2UserPrincipal,
        @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {
        userAuthService.logout(oauth2UserPrincipal, userDetails);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
        @RequestBody @Valid final RefreshTokenRequest refreshTokenRequest,
        @AuthenticationPrincipal final AuthenticatedUser authenticatedUser
    ) {
        final TokenRefreshResponse tokenRefreshResponse = userAuthService.refreshToken(refreshTokenRequest.refreshToken(), authenticatedUser.getProvider());
        return ResponseEntity.ok().body(tokenRefreshResponse);
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(
        @AuthenticationPrincipal final CustomUserDetails userDetails
    ) {
        userAuthService.deleteAccount(userDetails.getMemberId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unlink/{provider}")
    public ResponseEntity<Void> unlink(
        @AuthenticationPrincipal final OAuth2UserPrincipal oauth2UserPrincipal
    ) {
        userAuthService.unlink(oauth2UserPrincipal.getOauth2UserInfo().getProvider(), oauth2UserPrincipal);
        return ResponseEntity.ok().build();
    }
}
