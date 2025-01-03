package com.lolclone.chat_server.service;

import com.lolclone.chat_server.domain.Friend;
import com.lolclone.chat_server.domain.FriendRequest;
import com.lolclone.chat_server.domain.User;
import com.lolclone.chat_server.domain.UserStatus;
import com.lolclone.chat_server.dto.request.FriendSearchRequest;
import com.lolclone.chat_server.dto.response.FriendRequestResponseDto;
import com.lolclone.chat_server.dto.response.FriendResponseDto;
import com.lolclone.chat_server.repository.FriendRepository;
import com.lolclone.chat_server.repository.FriendRequestRepository;
import com.lolclone.chat_server.repository.UserRepository;
import com.lolclone.chat_server.validator.ChatValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.lolclone.chat_server.exception.common.BadRequestException;
import com.lolclone.chat_server.exception.common.NotFoundException;
import com.lolclone.chat_server.exception.domain.ExceptionType;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "friendSearchCache")
public class FriendService {
    private static final int MAX_PENDING_REQUESTS = 50;

    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ChatValidator chatValidator;
    
    /**
     * 사용자의 전체 친구 목록을 조회합니다.
     * @param userId 친구 목록을 조회할 사용자의 ID
     * @return 친구 목록 DTO 리스트
     */
    public List<FriendResponseDto> getFriendsList(final UUID userId) {
        final List<Friend> friends = friendRepository.findByUserId(userId);
        return friends.stream()
            .map(FriendResponseDto::from)
            .collect(Collectors.toList());
    }
    
    /**
     * 친구 목록을 한글 이름 순으로 정렬하여 반환합니다.
     * @param userId 친구 목록을 조회할 사용자의 ID
     * @return 이름순으로 정렬된 친구 목록 DTO 리스트
     */
    public List<FriendResponseDto> sortByAlphabetical(final UUID userId) {
        final List<Friend> friends = friendRepository.findByUserIdOrderByFriendNameAsc(userId);
        // db 쿼리를 통해 정렬을 해서 가져오는건 유니코드 기반이므로 한국어 고유의 순서를 보장하지 않을 수 있다.
        final Collator koreanCollator = Collator.getInstance(Locale.KOREAN);
        
        return friends.stream()
            .sorted(Comparator.comparing(Friend::getFriendName, koreanCollator)) //이 경우 스트림을 사용해서 정렬을 보장하자.
            .map(FriendResponseDto::from)
            .collect(Collectors.toList());
    }
    
    /**
     * 친구 목록을 상태순으로 정렬하여 반환합니다.
     * 정렬 우선순위: ONLINE > IN_GAME > OFFLINE
     * 같은 상태인 경우 한글 이름순으로 정렬됩니다.
     * @param userId 친구 목록을 조회할 사용자의 ID
     * @return 상태순으로 정렬된 친구 목록 DTO 리스트
     */
    public List<FriendResponseDto> sortByStatus(final UUID userId) {
        final List<Friend> friends = friendRepository.findByUserId(userId);
        final Collator koreanCollator = Collator.getInstance(Locale.KOREAN);
        
        return friends.stream()
            .sorted(Comparator
                .comparing(Friend::getStatus, (s1, s2) -> {
                    if (s1 == s2) return 0;
                    if (s1 == UserStatus.ONLINE) return -1;
                    if (s2 == UserStatus.ONLINE) return 1;
                    if (s1 == UserStatus.IN_GAME) return -1;
                    if (s2 == UserStatus.IN_GAME) return 1;
                    return 0;
                })
                .thenComparing(Friend::getFriendName, koreanCollator))
            .map(FriendResponseDto::from)
            .collect(Collectors.toList());
    }
    
    /**
     * 두 사용자가 친구 관계인지 확인합니다.
     * @param userId 첫 번째 사용자 ID
     * @param friendId 두 번째 사용자 ID
     * @return 친구 관계이면 true, 아니면 false
     */
    public boolean isFriend(final UUID userId, final UUID friendId) {
        return friendRepository.existsByUserIdAndFriendId(userId, friendId);
    }
    
    /**
     * 친구 추가 요청을 보냅니다.
     */
    @Transactional
    public void sendFriendRequest(final UUID senderId, final String receiverNickname) {
        final User receiver = userRepository.findByNickname(receiverNickname)
                .orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
        
        UUID receiverId = receiver.getId();
        // 입력값 기본 검증
        chatValidator.validateFriendRequest(senderId, receiverId);
        
        // 비즈니스 로직 검증
        if (isFriend(senderId, receiverId)) {
            throw new BadRequestException(ExceptionType.INVALID_REQUEST_ARGUMENT);
        }
        
        if (friendRequestRepository.existsBySenderIdAndReceiverId(senderId, receiverId)) {
            throw new BadRequestException(ExceptionType.INVALID_REQUEST_ARGUMENT);
        }
        
        if (friendRequestRepository.countByReceiverId(receiverId) >= MAX_PENDING_REQUESTS) {
            throw new BadRequestException(ExceptionType.INVALID_REQUEST_ARGUMENT);
        }
        
        FriendRequest.of(senderId, receiverId);
        notificationService.sendFriendRequestNotification(receiverId, senderId);
    }
    
    /**
     * 친구 요청을 수락합니다.
     */
    @Transactional
    public void acceptFriendRequest(final UUID receiverId, final UUID requesterId) {
        if (!friendRequestRepository.existsBySenderIdAndReceiverId(requesterId, receiverId)) {
            throw new NotFoundException(ExceptionType.CHAT_HISTORY_NOT_FOUND);
        }
        
        final User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.INVALID_RECEIVER));
        final User requester = userRepository.findById(requesterId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.INVALID_RECEIVER));
        
        Friend.of(receiver, requester);
        Friend.of(requester, receiver);
        
        friendRequestRepository.deleteBySenderIdAndReceiverId(requesterId, receiverId);
        notificationService.sendFriendAcceptNotification(requesterId, receiverId);
    }
    
    /**
     * 사용자의 친구 요청 대기 목록을 조회합니다.
     * @param userId 조회할 사용자의 ID
     * @return 친구 요청 DTO 리스트
     */
    public List<FriendRequestResponseDto> getPendingRequests(final UUID userId) {
        return friendRequestRepository.findByReceiverId(userId).stream()
            .map(FriendRequestResponseDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void blockFriend(final UUID userId, final UUID friendId) {
        final Friend friendship = friendRepository.findByUserIdAndFriendId(userId, friendId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.FRIEND_NOT_FOUND));
        friendship.setBlocked(true);
        friendRepository.save(friendship);
    }

    @Transactional
    public void unblockFriend(final UUID userId, final UUID friendId) {
        final Friend friendship = friendRepository.findByUserIdAndFriendId(userId, friendId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.FRIEND_NOT_FOUND));
        friendship.setBlocked(false);
        friendRepository.save(friendship);
    }

    @Transactional
    public void updateFriendMemo(final UUID userId, final UUID friendId, final String memo) {
        if (memo != null && memo.length() > 500) {
            throw new BadRequestException(ExceptionType.INVALID_REQUEST_ARGUMENT);
        }
        final Friend friendship = friendRepository.findByUserIdAndFriendId(userId, friendId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.FRIEND_NOT_FOUND));
        friendship.setMemo(memo);
        friendRepository.save(friendship);
    }

    @Transactional
    public void deleteFriend(final UUID userId, final UUID friendId) {
        final Friend friendship = friendRepository.findByUserIdAndFriendId(userId, friendId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.FRIEND_NOT_FOUND));
        friendRepository.delete(friendship);
    }

    public FriendResponseDto getFriendInfo(final UUID userId, final UUID friendId) {
        final Friend friendship = friendRepository.findByUserIdAndFriendId(userId, friendId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.FRIEND_NOT_FOUND));
        
        return FriendResponseDto.from(friendship);
    }

    /**
     * 특정 사용자가 다른 사용자를 차단했는지 확인합니다.
     * @param userId 확인할 사용자의 ID
     * @param targetId 차단 여부를 확인할 대상의 ID
     * @return 차단되었다면 true, 아니면 false
     */
    public boolean isBlocked(final UUID userId, final UUID targetId) {
        return friendRepository.findByUserIdAndFriendId(userId, targetId)
            .map(Friend::isBlocked)
            .orElse(false);
    }

    public void sendGameInvitation(final UUID senderId, final UUID receiverId, final UUID roomId, final String message) {
        final boolean isBlocked = isBlocked(senderId, receiverId);
        notificationService.sendGameInvitation(senderId, receiverId, roomId, message, isBlocked);
    }

    // 친구 검색
    @Transactional(readOnly = true)
    public List<FriendResponseDto> searchFriends(final UUID userId, final String nickname, final String tag) {
        final List<User> friends = userRepository.findByNicknameContainingAndTagContaining(nickname, tag);
        return friends.stream()
            .filter(friend -> friendRepository.existsByUserIdAndFriendId(userId, friend.getId()))
            .map(friend -> friendRepository.findByUserIdAndFriendId(userId, friend.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(FriendResponseDto::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FriendResponseDto> searchFriendsInMessageWindow(final FriendSearchRequest request) {
        if (request.query() == null || request.query().trim().isEmpty()) {
            return Collections.emptyList();
        }

        // 최대 검색 결과 수 제한
        final int MAX_SEARCH_RESULTS = 10;
        
        return userRepository.searchFriendsForMessageWindow(
                request.userId(),
                request.query().trim(),
                MAX_SEARCH_RESULTS
            ).stream()
            .map(user -> friendRepository.findByUserIdAndFriendId(request.userId(), user.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(FriendResponseDto::from)
            .collect(Collectors.toList());
    }
}
