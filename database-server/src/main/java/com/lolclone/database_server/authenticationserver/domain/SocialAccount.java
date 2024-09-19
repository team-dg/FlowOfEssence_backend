package com.lolclone.database_server.authenticationserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialAccount {

    private static final int MAX_NICKNAME_LENGTH = 30;
    private static final int MAX_PROFILE_IMAGE_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_account_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    @NotNull
    @Size(max = MAX_NICKNAME_LENGTH)
    private String nickname;

    @NotNull
    @Size(max = MAX_PROFILE_IMAGE_LENGTH)
    @Column(name = "profile_image")
    private String profileImage;

    public SocialAccount(final SocialType socialType, final String socialId, final Member member) {
        this.socialType = socialType;
        this.socialId = socialId;
    }
}
