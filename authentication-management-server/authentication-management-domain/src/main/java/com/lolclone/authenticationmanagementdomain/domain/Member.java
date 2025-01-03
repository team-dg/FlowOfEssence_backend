package com.lolclone.authenticationmanagementdomain.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lolclone.authenticationmanagementdomain.common.BaseTimeEntity;
import com.lolclone.authenticationmanagementdomain.exception.UnsupportedStateTransitionException;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.SocialType;
import com.lolclone.commonmodule.authenticationmanagementserver.domain.UserInfo;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Collections.singletonList;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    
    private static final int MAX_USER_ID_LENGTH = 255;
    private static final int MAX_SOCIAL_ID_LENGTH = 255;
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

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "registration_type", columnDefinition = "varchar")
    private RegistrationType registrationType = RegistrationType.STANDARD;

    @Column(name = "user_created")
    private boolean userCreated = false;

    private LocalDateTime deletedAt;

    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus = UserStatus.STARTED;

    public static ResultWithDomainEvents<Member, MemberDomainEvent> createSocialUser(UserInfo userInfo) {
        Member member = Member.fromUserInfo(userInfo);
        
        switch (member.userStatus) {
            case STARTED:
                member.userStatus = UserStatus.CREATING_USER;
                List<MemberDomainEvent> events = singletonList(new UserCreationStartedEvent(member.id, member.nickname));
                return new ResultWithDomainEvents<>(member, events);
            default:
                throw new UnsupportedStateTransitionException(member.userStatus);
        }
    }

    public static ResultWithDomainEvents<Member, MemberDomainEvent> createUser(String username, String password, String email, String nickname) {
        Member member = Member.of(username, password, email, nickname);
        switch (member.userStatus) {
            case STARTED:
                member.userStatus = UserStatus.CREATING_USER;
                List<MemberDomainEvent> events = singletonList(new UserCreationStartedEvent(member.id, member.nickname));
                return new ResultWithDomainEvents<>(member, events);
            default:
                throw new UnsupportedStateTransitionException(member.userStatus);
        }
    }

    public List<MemberDomainEvent> noteUserCreated() {
        switch (userStatus) {
            case CREATING_USER:
                this.userStatus = UserStatus.COMPLETED;
                this.userCreated = true;
                return singletonList(new UserCreationCompleted());
            default:
                throw new UnsupportedStateTransitionException(userStatus);
        }
    }

    public List<MemberDomainEvent> failSignUp() {
        switch (userStatus) {
            case CREATING_USER:
                this.userStatus = UserStatus.FAILED;
                return singletonList(new UserCreationFailed());
            default:
                throw new UnsupportedStateTransitionException(userStatus);
        }
    }

    // 소셜 로그인용 생성자
    public Member(String socialId, SocialType socialType, RegistrationType registrationType) {
        this.id = UUID.randomUUID();
        this.socialId = socialId;
        this.socialType = socialType;
        this.registrationType = registrationType;
    }

    // 일반 로그인용 생성자
    public Member(String username, String password,  String email, String nickname, RegistrationType registrationType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.registrationType = registrationType;
        this.socialType = SocialType.NONE;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean matchPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }

    public static Member of(String username, String password, String email, String nickName) {
        return new Member(username, password, email, nickName, RegistrationType.STANDARD);
    }

    public static Member fromUserInfo(UserInfo userInfo) {
        return new Member(
            userInfo.socialId(),
            userInfo.socialType(),
            RegistrationType.SOCIAL
        );
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
