package com.lolclone.chatinfra.service.domain;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.Friend;
import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatdomain.repository.FriendRepository;
import com.lolclone.chatinfra.exception.commonexception.BadRequestException;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FriendService {
    private final FriendRepository friendRepository;

    public Friend getOrThrow(final UUID id) {
        return friendRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.FRIEND_NOT_FOUND));
    }

    /**
     * 친구 관계 생성
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public Friend createFriendship(final Member user, final Member friend) {
        validateNotAlreadyFriends(user, friend);
        Friend friendship = Friend.of(user, friend);
        return friendRepository.save(friendship);
    }

    /**
     * 친구 차단
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void blockFriend(final UUID friendId) {
        Friend friend = getOrThrow(friendId);
        friend.block();
    }

    /**
     * 친구 차단 해제
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void unblockFriend(final UUID friendId) {
        Friend friend = getOrThrow(friendId);
        friend.unblock();
    }

    /**
     * 친구 메모 수정
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateMemo(final UUID friendId, final String memo) {
        Friend friend = getOrThrow(friendId);
        friend.updateMemo(memo);
    }

    /**
     * 차단되지 않은 사용자의 모든 친구 목록 조회
     */
    public List<Friend> getActiveFriends(final Member user) {
        return friendRepository.findByUserAndBlockedFalseAndBlockedByFalse(user);
    }

    /**
     * 사용자의 차단된 친구 목록 조회
     */
    public List<Friend> getBlockedFriends(final Member user) {
        return friendRepository.findByUserAndIsBlockedTrue(user);
    }

    /**
     * 친구 관계인지 확인
     * @return 친구인 경우 true, 아닌 경우 false
     */
    public boolean areFriends(final Member user, final Member otherUser) {
        return friendRepository.findByUserAndFriend(user, otherUser).isPresent();
    }

    /**
     * 친구 관계인지 확인
     * 
     * @return 친구인 경우 Friend, 아닌 경우 null
     */
    public Friend findFriendship(final Member user, final Member otherUser) {
        return friendRepository.findByUserAndFriend(user, otherUser).orElse(null);
    }

    /**
     * 친구가 차단되었는지 확인
     */
    public boolean isBlocked(final UUID friendId) {
        Friend friend = getOrThrow(friendId);
        return friend.isBlocked();
    }

    private void validateNotAlreadyFriends(final Member user, final Member friend) {
        if (areFriends(user, friend)) {
            throw new BadRequestException(ExceptionType.ALREADY_FRIENDS);
        }
    }

    /**
     * 친구 관계 확인 (친구가 아닌 경우 예외 발생)
     */
    public void validateFriendship(final Member user, final Member otherUser) {
        if (!areFriends(user, otherUser)) {
            throw new BadRequestException(ExceptionType.NOT_FRIEND);
        }
    }

    /**
     * 차단되지 않은 활성 친구 목록을 닉네임순으로 정렬하여 조회
     * @param user 사용자
     * @return 닉네임순으로 정렬된 친구 목록
     */
    public List<Friend> getActiveFriendsSortedByNickname(final Member user) {
        List<Friend> friends = friendRepository.findByUserAndIsBlockedFalse(user);
        return friends.stream()
            .sorted((f1, f2) -> {
                String nick1 = f1.getFriend().getNickname();
                String nick2 = f2.getFriend().getNickname();
                return Collator.getInstance(Locale.KOREAN).compare(nick1, nick2);
            })
            .collect(Collectors.toList());
    }

    /**
     * 친구 관계 삭제
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteFriendship(final Member user, final Member friend) {
        friendRepository.findByUserAndFriend(user, friend)
                .ifPresent(friendship -> friendRepository.delete(friendship));
    }

    /**
     * 닉네임으로 친구 검색
     */
    public List<Friend> searchFriends(final Member user, final String nicknameKeyword, final String tagKeyword) {
        List<Friend> friends = friendRepository.findByUserAndIsBlockedFalse(user);
        return friends.stream()
                .filter(friend -> {
                    boolean matchesNickname = nicknameKeyword == null ||
                            friend.getFriend().getNickname().toLowerCase().contains(nicknameKeyword.toLowerCase());
                    boolean matchesTag = tagKeyword == null ||
                            friend.getFriend().hasTag(tagKeyword);
                    return matchesNickname && matchesTag;
                })
                .collect(Collectors.toList());
    }
}
