package com.lolclone.authenticationmanagementdomain.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lolclone.authenticationmanagementdomain.common.BaseTimeEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    
    private static final int MAX_USER_ID_LENGTH = 255;
    private static final int MAX_PASSWORD_LENGTH = 255;
    private static final int MAX_NICKNAME_LENGTH = 255;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "user_id", columnDefinition = "uuid")
    private UUID id;

    @Size(max = MAX_USER_ID_LENGTH)
    @Column(name = "username", unique = true)
    private String username;

    @Size(max = MAX_PASSWORD_LENGTH)
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    @Column(name = "email_verified")
    private boolean emailVerified = false;

    @Size(max = MAX_NICKNAME_LENGTH)
    @Column(name = "nickname")
    private String nickname;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar")
    private UserRole role = UserRole.ROLE_USER;

    @Column(name = "user_created")
    private boolean userCreated = false;

    private LocalDateTime deletedAt;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "social_name", nullable = false, unique = true)
    private String socialName;

    @Builder
    public Member(String username, String password, String email, String nickname, String socialName, String profileImageUrl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.socialName = socialName;
        this.profileImageUrl = profileImageUrl;
    }

    public static Member of(String email, String socialName, String nickname, String profileImageUrl) {
        return Member.builder()
            .email(email)
            .socialName(socialName)
            .nickname(nickname)
            .profileImageUrl(profileImageUrl)
            .build();
    }

    public void updateUserInfo(String email, String nickname, String profileImageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    public List<String> getAuthorityStrings() {
        return getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    }

    public boolean matchPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
