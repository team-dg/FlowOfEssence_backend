package com.lolclone.chatinfra.service.domain;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.friend.Friend;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.repository.MemberRepository;
import com.lolclone.chatdomain.repository.friend.FriendRepository;
import com.lolclone.chatdomain.repository.friend.query.FriendChatInfoDto;
import com.lolclone.chatdomain.repository.friend.query.FriendSortCondition;
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
    private final MemberRepository memberRepository;

    private static final int MAX_FRIEND_COUNT = 300;

    // 내부 헬퍼 메서드
    private Member getOrElse(UUID userId) {
        return memberRepository.findById(userId).orElseThrow(() -> new NotFoundException(ExceptionType.MEMBER_NOT_FOUND));
    }

    public Page<FriendChatInfoDto> getFriendList(
        final UUID userId, 
        final FriendSortCondition sortCondition, 
        final Pageable pageable
    ) {
        getOrElse(userId);
        validateFriendCount(userId);
        return friendRepository.findFriends(userId, sortCondition, pageable);
    }

    private void validateFriendCount(UUID userId) {
        long totalCount = friendRepository.countFriends(userId).fetchOne();
        if (totalCount > MAX_FRIEND_COUNT) {
            throw new IllegalStateException("친구 수가 최대 제한(" + MAX_FRIEND_COUNT + "명)을 초과했습니다.");
        }
    }



    // /**
    //  * 친구 관계 생성
    //  */
    // public Friend createFriendship(MemberId userId, MemberId friendId) {
    //     // 사용자와 친구 조회
    //     Member user = memberRepository.findById(userId)
    //             .orElseThrow(() -> new NotFoundException(ExceptionType.MEMBER_NOT_FOUND));
    //     Member friend = memberRepository.findById(friendId)
    //             .orElseThrow(() -> new NotFoundException(ExceptionType.MEMBER_NOT_FOUND));

    //     // 이미 존재하는 친구 관계 확인
    //     validateNoExistingFriendship(userId, friendId);

    //     // 친구 관계 생성
    //     Friend friendship = Friend.create(user, friend);
    //     Friend savedFriendship = friendRepository.save(friendship);

    //     // 이벤트 발행
    //     // eventPublisher.publish(new FriendshipCreatedEvent(userId, friendId));

    //     return savedFriendship;
    // }

    // /**
    //  * 친구 메모 수정
    //  */
    // public void updateMemo(MemberId userId, MemberId friendId, String memo) {
    //     Friend friendship = findActiveFriendship(userId, friendId);
    //     friendship.updateMemo(memo);
    //     friendRepository.save(friendship);
    // }

    // @Transactional(readOnly = true)
    // public List<Friend> getActiveFriendsSortedByNickname(MemberId userId) {
    //     return friendRepository.findActiveFriendsByUserId(userId)
    //             .stream()
    //             .sorted(Friend.compareByNickname())
    //             .collect(Collectors.toList());
    // }

    // /**
    //  * 친구 검색
    //  */
    // @Transactional(readOnly = true)
    // public List<Friend> searchFriends(MemberId userId, FriendshipCriteria criteria) {
    //     return friendRepository.findActiveFriendsByUserId(userId)
    //         .stream()
    //         .filter(friend -> friend.matches(criteria))
    //         .collect(Collectors.toList());
    // }

    // /**
    //  * 친구 차단
    //  */
    // public void blockFriend(MemberId userId, MemberId friendId) {
    //     Friend friendship = findActiveFriendship(userId, friendId);
    //     friendship.block();
    //     friendRepository.save(friendship);
    //     // eventPublisher.publish(new FriendBlockedEvent(userId, friendId));
    // }

    // /**
    //  * 친구 차단 해제
    //  */
    // public void unblockFriend(MemberId userId, MemberId friendId) {
    //     Friend friendship = findBlockedFriendship(userId, friendId);
    //     friendship.unblock();
    //     friendRepository.save(friendship);
    //     // eventPublisher.publish(new FriendUnblockedEvent(userId, friendId));
    // }

    // /**
    //  * 친구 관계 삭제
    //  */
    // public void unfriend(MemberId userId, MemberId friendId) {
    //     Friend friendship = findActiveFriendship(userId, friendId);
    //     friendship.unfriend();
    //     friendRepository.save(friendship);
    //     // eventPublisher.publish(new FriendshipEndedEvent(userId, friendId));
    // }

    // /**
    //  * 친구 목록 조회
    //  */
    // @Transactional(readOnly = true)
    // public List<Friend> getFriendList(MemberId userId, FriendshipCriteria criteria) {
    //     return friendRepository.findAllByUserIdAndCriteria(userId, criteria);
    // }

    // /**
    //  * 상호작용 업데이트
    //  */
    // public void updateInteraction(MemberId userId, MemberId friendId) {
    //     Friend friendship = findActiveFriendship(userId, friendId);
    //     friendship.updateLastInteraction();
    //     friendRepository.save(friendship);
    // }

    // // 내부 헬퍼 메서드
    // private Friend findActiveFriendship(MemberId userId, MemberId friendId) {
    //     return friendRepository.findByUserIdAndFriendId(userId, friendId)
    //         .filter(Friend::isActive)
    //         .orElseThrow(() -> new NotFoundException(ExceptionType.FRIEND_NOT_FOUND));
    // }

    // private Friend findBlockedFriendship(MemberId userId, MemberId friendId) {
    //     return friendRepository.findByUserIdAndFriendId(userId, friendId)
    //         .filter(Friend::isBlocked)
    //         .orElseThrow(() -> new NotFoundException(ExceptionType.FRIEND_NOT_FOUND));
    // }

    // private void validateNoExistingFriendship(MemberId userId, MemberId friendId) {
    //     if (friendRepository.existsByUserIdAndFriendId(userId, friendId)) {
    //         throw new BadRequestException(ExceptionType.ALREADY_FRIENDS);
    //     }
    // }
}
