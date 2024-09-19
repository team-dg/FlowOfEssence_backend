package com.lolclone.database_server.authenticationserver.domain;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

import com.lolclone.common_module.util.Validator;
import com.lolclone.database_server.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    
    private static final int MAX_USER_ID_LENGTH = 255;
    private static final int MAX_SOCIAL_ID_LENGTH = 255;
    private static final int MAX_NICKNAME_LENGTH = 30;
    private static final int MAX_PROFILE_IMAGE_LENGTH = 255;
    private static final int MAX_PASSWORD_LENGTH = 255;
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Size(max = MAX_SOCIAL_ID_LENGTH)
    @Column(name = "social_id")
    private String socialId;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_type", columnDefinition = "varchar")
    private SocialType socialType;

    @Size(max = MAX_USER_ID_LENGTH)
    @Column(name = "username", unique = true)
    private String username;

    @Size(max = MAX_PASSWORD_LENGTH)
    private String password;

    @Size(max = MAX_NICKNAME_LENGTH)
    private String nickname;

    @Size(max = MAX_PROFILE_IMAGE_LENGTH)
    @Column(name = "profile_image")
    private String profileImage;

    @Email
    @Column(unique = true)
    private String email;

    @Column(name = "email_verified")
    private boolean emailVerified = false;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    private int level;

    private int exp;

    private int totalPlayTime;

    // @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "user_id")
    // private List<Rank> ranks = new ArrayList<>();

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar")
    private UserRole role = UserRole.ROLE_USER;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar")
    private UserStatus status = UserStatus.OFFLINE;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "registration_type", columnDefinition = "varchar")
    private RegistrationType registrationType = RegistrationType.STANDARD;

    @Column(name = "account_active")
    private boolean accountActive = true;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    private LocalDateTime deletedAt;

    @ElementCollection
    @CollectionTable(name = "social_platform_info", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "platform")
    @Column(name = "platform_specific_info")
    private Map<SocialType, String> socialPlatformInfo = new EnumMap<>(SocialType.class);

    // 소셜 로그인용 생성자
    public Member(String socialId, SocialType socialType, String nickname, String profileImage, RegistrationType registrationType) {
        this.socialId = socialId;
        this.socialType = socialType;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.registrationType = registrationType;
    }

    // 일반 로그인용 생성자
    public Member(String username, String password, String nickname, String profileImage, String email, RegistrationType registrationType) {
        validate(username, password, nickname, profileImage, email, registrationType);
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.email = email;
        this.registrationType = registrationType;
        this.socialType = SocialType.NONE;
    }

    // 유효성 검사 (등록 유형에 따라 다르게 검사)
    private void validate(String username, String password, String nickname, String profileImage, String email, RegistrationType registrationType) {
        if (registrationType == RegistrationType.STANDARD) {
            validateUsername(username);
            validatePassword(password);
            validateEmail(email);
        }
        validateNickname(nickname);
        validateProfileImage(profileImage);
    }

    private void validateUsername(String username) {
        String fieldName = "username";
        Validator.notBlank(username, fieldName);
        Validator.maxLength(username, MAX_USER_ID_LENGTH, fieldName);
    }

    private void validatePassword(String password) {
        String fieldName = "password";
        Validator.notBlank(password, fieldName);
        Validator.maxLength(password, MAX_PASSWORD_LENGTH, fieldName);
    }

    private void validateNickname(String nickname) {
        String fieldName = "nickname";
        Validator.maxLength(nickname, MAX_NICKNAME_LENGTH, fieldName);
        Validator.notBlank(nickname, fieldName);
    }

    private void validateProfileImage(String profileImage) {
        Validator.maxLength(profileImage, MAX_PROFILE_IMAGE_LENGTH, "profileImage");
    }

    private void validateEmail(String email) {
        Validator.notBlank(email, "email");
        Validator.email(email, "email");
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean matchPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }

    public static Member of(String username, String password, String nickname, String email) {
        return new Member(username, password, nickname, null, email, RegistrationType.STANDARD);
    }
}
