package com.lolclone.authenticationmanagementinfra.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lolclone.authenticationmanagementinfra.service.application.OAuth2AuthenticationFacade;
import com.lolclone.authenticationmanagementserviceapi.dto.CreateMemberResponse;
import com.lolclone.authenticationmanagementserviceapi.dto.LoginRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.LoginResponse;
import com.lolclone.authenticationmanagementserviceapi.dto.LogoutRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.OAuth2LoginRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.OpenIdLoginRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.RefreshTokenRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.SignUpRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.TokenRefreshResponse;
import com.lolclone.commonmodule.apigatewayserver.annotation.UserAuth;
import com.lolclone.commonmodule.apigatewayserver.domain.MemberAuthentication;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.SocialType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthController {
    private final OAuth2AuthenticationFacade oAuth2AuthenticationFacade;

    @PostMapping("/login/oauth2")
    public ResponseEntity<CreateMemberResponse> oauth2Login(
        @Valid @RequestBody final OAuth2LoginRequest oauth2LoginRequest
    ) {
        final UUID userId = oAuth2AuthenticationFacade.oAuth2Login(oauth2LoginRequest.socialType(), oauth2LoginRequest.code());
        return ResponseEntity.ok().body(new CreateMemberResponse(userId));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody final LoginRequest loginRequest
    ) {
        final LoginResponse loginResponse = oAuth2AuthenticationFacade.originalLogin(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<CreateMemberResponse> signUp(
        @Valid @RequestBody final SignUpRequest signUpRequest
    ) {
        final UUID userId = oAuth2AuthenticationFacade.originalSignUp(signUpRequest);
        return ResponseEntity.ok().body(new CreateMemberResponse(userId)); 
    }

    @GetMapping("/login/oauth2/{socialType}")
    public ResponseEntity<CreateMemberResponse> oauth2LoginRedirect(
        @PathVariable final SocialType socialType,
        @RequestParam final String code
    ) {
        final UUID userId = oAuth2AuthenticationFacade.oAuth2Login(socialType, code);
        return ResponseEntity.ok().body(new CreateMemberResponse(userId));
    }

    @PostMapping("/login/open-id")
    public ResponseEntity<CreateMemberResponse> openIdLogin(
        @Valid @RequestBody final OpenIdLoginRequest openIdLoginRequest
    ) {
        final UUID userId = oAuth2AuthenticationFacade.openIdLogin(openIdLoginRequest.socialType(), openIdLoginRequest.idToken());
        return ResponseEntity.ok().body(new CreateMemberResponse(userId));
    }

    @UserAuth
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        final MemberAuthentication memberAuthentication,
        @RequestBody @Valid final LogoutRequest logoutRequest
    ) {
        oAuth2AuthenticationFacade.logOut(memberAuthentication.getId(), UUID.fromString(logoutRequest.refreshToken()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(
        @RequestBody @Valid final RefreshTokenRequest refreshTokenRequest
    ) {
        final TokenRefreshResponse tokenRefreshResponse = oAuth2AuthenticationFacade.refresh(UUID.fromString(refreshTokenRequest.refreshToken()));
        return ResponseEntity.ok().body(tokenRefreshResponse);
    }
    
    @UserAuth
    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(final MemberAuthentication memberAuthentication) {
        oAuth2AuthenticationFacade.deleteAccount(memberAuthentication.getId());
        return ResponseEntity.ok().build();
    }
}
