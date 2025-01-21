package com.lolclone.chatdomain.domain.friend;

import java.text.Collator;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Locale;

import com.lolclone.chatdomain.common.BaseTimeEntity;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.exception.InvalidFriendshipException;
import com.lolclone.chatdomain.exception.InvalidFriendshipStatusException;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity 책임
 * 친구 관계 관리(친구 요청, 삭제)
 * 친구 목록 조회
 */

@Entity
@Table(name = "friends")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseTimeEntity {
    @EmbeddedId
    private FriendId id; // 복합키를 값 객체로 분리

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member user;

    @MapsId("friendId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private Member friend;

    @Embedded
    private FriendshipStatus status; // 상태를 값 객체로 분리

    /**
     * 👥 친구와의 마지막 상호작용 시간을 저장하는 필드
     * - 채팅, 게임 초대 등의 상호작용이 발생할 때마다 업데이트
     * - 친구 목록 정렬 시 최근 상호작용 순으로 정렬하는데 사용
     */
    @Column(name = "last_interaction_at") 
    private LocalDateTime lastInteractionAt;

    @Column(name = "memo")
    private String memo; 

    @Builder
    private Friend(Member user, Member friend) {
        this.id = FriendId.of(user.getId(), friend.getId());
        this.user = user;
        this.friend = friend;
        this.status = FriendshipStatus.active();
    }

    // 정적 팩토리 메서드
    public static Friend create(Member user, Member friend) {
        validateFriendship(user, friend);
        return new Friend(user, friend);
    }

    // 비즈니스 메서드
    public void block() {
        validateActiveStatus();
        this.status = this.status.block();
    }

    public void unblock() {
        validateBlockedStatus();
        this.status = this.status.unblock();
    }

    public void unfriend() {
        validateActiveStatus();
        this.status = this.status.unfriend();
    }

    // 검증 메서드
    private static void validateFriendship(Member user, Member friend) {
        if (user.equals(friend)) {
            throw new InvalidFriendshipException("자기 자신과 친구가 될 수 없습니다.");
        }
    }

    private void validateActiveStatus() {
        if (!this.status.isActive()) {
            throw new InvalidFriendshipStatusException("활성 상태가 아닌 친구 관계입니다.");
        }
    }

    private void validateBlockedStatus() {
        if (!this.status.isBlocked()) {
            throw new InvalidFriendshipStatusException("차단 상태가 아닌 친구 관계입니다.");
        }
    }

    // 상태 확인 메서드
    public boolean isActive() {
        return this.status.isActive();
    }

    public boolean isBlocked() {
        return this.status.isBlocked();
    }

    public boolean involves(Member member) {
        return this.user.equals(member) || this.friend.equals(member);
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }

    public boolean matches(FriendshipCriteria criteria) {
        return matchesNickname(criteria.getNicknameKeyword()) &&
               matchesTag(criteria.getTagKeyword());
    }

    private boolean matchesNickname(String keyword) {
        return keyword == null || 
               friend.getNickname().toLowerCase().contains(keyword.toLowerCase());
    }

    private boolean matchesTag(String keyword) {
        return keyword == null || friend.hasTag(keyword);
    }

    public static Comparator<Friend> compareByNickname() {
        return (f1, f2) -> Collator.getInstance(Locale.KOREAN)
            .compare(f1.getFriend().getNickname(), f2.getFriend().getNickname());
    }
}
