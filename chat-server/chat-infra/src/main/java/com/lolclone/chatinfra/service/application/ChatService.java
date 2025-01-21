package com.lolclone.chatinfra.service.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.FriendFolder;
import com.lolclone.chatdomain.domain.FriendInFolder;
import com.lolclone.chatdomain.domain.MemberStatus;
import com.lolclone.chatdomain.domain.chatparticipant.ChatParticipant;
import com.lolclone.chatdomain.domain.chatroom.ChatRoom;
import com.lolclone.chatdomain.domain.friend.Friend;
import com.lolclone.chatdomain.domain.friendrequest.FriendRequest;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.domain.message.Message;
import com.lolclone.chatdomain.domain.message.MessageType;
import com.lolclone.chatdomain.repository.friend.FriendRepository;
import com.lolclone.chatdomain.repository.friend.query.FriendChatInfoDto;
import com.lolclone.chatinfra.exception.commonexception.BadRequestException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;
import com.lolclone.chatinfra.service.domain.ChatParticipantService;
import com.lolclone.chatinfra.service.domain.ChatRoomService;
import com.lolclone.chatinfra.service.domain.FriendFolderService;
import com.lolclone.chatinfra.service.domain.FriendInFolderService;
import com.lolclone.chatinfra.service.domain.FriendRequestService;
import com.lolclone.chatinfra.service.domain.FriendService;
import com.lolclone.chatinfra.service.domain.MemberService;
import com.lolclone.chatinfra.service.domain.MessageService;
import com.lolclone.chatserviceapi.dto.UserSearchResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRoomService chatRoomService;
    private final ChatParticipantService chatParticipantService;
    private final MessageService messageService;
    private final MemberService memberService;
    private final FriendService friendService;
    private final FriendRequestService friendRequestService;
    private final FriendInFolderService friendInFolderService;
    private final FriendFolderService friendFolderService;
    private final FriendRepository friendRepository;

    private static final int MAX_PENDING_REQUESTS = 50; // 최대 대기 요청 수
    private static final int MAX_FRIEND_COUNT = 300;

    public Page<FriendChatInfoDto> getFriendListByNickname(
        final UUID userId, 
        final boolean sortByNickname, 
        final Pageable pageable
    ) {
        validateFriendCount(userId);
        return friendRepository.findFriendByNickname(userId, sortByNickname, pageable);
    }

    private void validateFriendCount(UUID userId) {
        long totalCount = friendRepository.countFriends(userId);
        if (totalCount > MAX_FRIEND_COUNT) {
            throw new IllegalStateException("친구 수가 최대 제한(" + MAX_FRIEND_COUNT + "명)을 초과했습니다.");
        }
    }


    // /**
    //  * 친구와의 1대1 채팅방 생성 또는 조회
    //  * @param userId 사용자 ID
    //  * @param friendId 친구 ID
    //  * @return 생성되거나 조회된 채팅방
    //  */
    // @Transactional
    // public ChatRoom getOrCreatePersonalChatRoom(final UUID userId, final UUID friendId) {
    //     // 사용자와 친구 정보 조회 -> 없으면 예외 발생
    //     Map<UUID, Member> members = memberService.getMembersByIds(Arrays.asList(userId, friendId));
    //     Member user = members.get(userId);
    //     Member friend = members.get(friendId);
        
    //     // 친구 관계 확인 -> 친구가 아니면 예외 발생
    //     friendService.validateFriendship(user, friend);
        
    //     // 기존 1대1 채팅방 참여하고 있는게 있는지 조회
    //     return chatRoomService.getPersonalRoom(user, friend);
    // }

    // /**
    //  * 날짜별 채팅 메시지 조회
    //  * @param roomId 채팅방 ID
    //  * @param date 조회할 날짜
    //  * @return 해당 날짜의 메시지 목록
    //  */
    // public List<Message> getChatMessagesByDate(final UUID roomId, final LocalDate date) {
    //     return messageService.getMessagesByRoomAndDate(roomId, date);
    // }

    // /**
    //  * 일정 기간이 지난 메시지 삭제
    //  * @param days 보관 기간 (일)
    //  */
    // @Transactional
    // @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    // public void deleteOldMessages(final int days) {
    //     LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        
    //     // 삭제할 메시지 조회
    //     List<Message> oldMessages = messageService.getMessagesBeforeDate(cutoffDate);
        
    //     // 메시지 삭제
    //     for (Message message : oldMessages) {
    //         messageService.deleteMessage(message.getId());
    //         log.debug("메시지 삭제: messageId={}", message.getId());
    //     }
        
    //     log.info("오래된 메시지 삭제 완료: 삭제된 메시지 수={}", oldMessages.size());
    // }

    // /**
    //  * 친구와의 1대1 채팅 메시지 전송
    //  * @param roomId 채팅방 ID
    //  * @param senderId 발신자 ID
    //  * @param content 메시지 내용
    //  * @return 전송된 메시지
    //  */
    // @Transactional
    // public Message sendPersonalChatMessage(final UUID roomId, final UUID senderId, final String content) {
    //     // 채팅방 조회 및 개인 채팅방 여부 확인
    //     ChatRoom chatRoom = chatRoomService.getOrThrow(roomId);
    //     if (!chatRoom.isPersonal()) {
    //         throw new BadRequestException(ExceptionType.NOT_PERSONAL_CHAT_ROOM);
    //     }
        
    //     // 발신자 조회 및 채팅방 참여자 확인
    //     Member sender = memberService.getOrThrow(senderId);
    //     if (!chatParticipantService.isParticipant(chatRoom, sender)) {
    //         throw new BadRequestException(ExceptionType.NOT_CHAT_PARTICIPANT);
    //     }

    //     // 수신자 조회 (채팅방의 다른 참여자)
    //     Member recipient = chatParticipantService.getOtherParticipant(chatRoom, sender)
    //     .orElseThrow(() -> new BadRequestException(ExceptionType.RECIPIENT_NOT_FOUND));
        
    //     // 차단 여부 확인
    //     Friend friendship = friendService.findFriendship(sender, recipient);
    //     if (friendService.isBlocked(friendship.getId())) {
    //         throw new BadRequestException(ExceptionType.BLOCKED_USER);
    //     }

    //     // 상대방이 나를 차단했는지 확인
    //     Friend reverseFriendship = friendService.findFriendship(recipient, sender);
    //     if (friendService.isBlocked(reverseFriendship.getId())) {
    //         throw new BadRequestException(ExceptionType.BLOCKED_BY_USER);
    //     }

    //     // 메시지 전송
    //     Message message = messageService.createMessage(chatRoom, sender, recipient, content, MessageType.TEXT);
    //     chatRoomService.updateLastMessage(roomId, message);
    //     return message;
    // }

    // /**
    //  * 1대1 채팅방의 모든 대화 기록 삭제
    //  * @param roomId 채팅방 ID
    //  * @param userId 삭제 요청 사용자 ID
    //  */
    // @Transactional
    // public void deleteAllPersonalChatMessages(final UUID roomId, final UUID userId) {
    //     // 채팅방 조회
    //     ChatRoom chatRoom = chatRoomService.getOrThrow(roomId);
        
    //     // 1대1 채팅방 여부 확인
    //     if (!chatRoom.isPersonal()) {
    //     throw new BadRequestException(ExceptionType.NOT_PERSONAL_CHAT_ROOM);
    //     }
        
    //     // 사용자 조회 및 채팅방 참여자 확인
    //     Member user = memberService.getOrThrow(userId);
    //     if (!chatParticipantService.isParticipant(chatRoom, user)) {
    //         throw new BadRequestException(ExceptionType.NOT_CHAT_PARTICIPANT);
    //     }
        
    //     // 모든 메시지 삭제
    //     messageService.deleteAllMessages(roomId);
        
    //     // 채팅방의 마지막 메시지 초기화
    //     chatRoomService.clearLastMessage(roomId);
    // }

    // /**
    //  * 사용자의 친구 목록을 조회 (정렬 옵션 지원)
    //  * 차단된 친구는 목록에서 제외됩니다.
    //  * @param userId         사용자 ID
    //  * @param sortByNickname 가나다순 정렬 여부
    //  * @return 정렬된 친구 목록과 해당 친구와의 채팅방 정보
    //  */
    // public List<FriendChatInfoDto> getFriendList(final UUID userId, final boolean sortByNickname) {
    //     Member user = memberService.getOrThrow(userId);
    //     List<Friend> friends = sortByNickname
    //             ? friendService.getActiveFriendsSortedByNickname(user)
    //             : friendService.getActiveFriends(user);
        
    //     // 친구 목록을 FriendChatInfo로 변환 (채팅방 정보 포함)
    //     List<FriendChatInfoDto> friendChatInfos = friends.stream()
    //             .map(friend -> convertToFriendChatInfo(friend, user))
    //             .collect(Collectors.toList());
        
    //     log.info("친구 목록 가나다순 정렬 완료: userId={}, friendCount={}", userId, friendChatInfos.size());
    //     return friendChatInfos;
    // }

    // /**
    //  * 사용자의 친구 목록을 상태별로 정렬하여 조회
    //  * @param userId 사용자 ID
    //  * @return 상태별로 정렬된 친구 목록과 해당 친구와의 채팅방 정보
    //  */
    // public List<FriendChatInfoDto> getFriendListSortedByStatus(final UUID userId) {
    //     Member user = memberService.getOrThrow(userId);
    //     List<Friend> friends = friendService.getActiveFriends(user);
        
    //     // 친구 목록을 FriendChatInfo로 변환하고 상태별로 정렬
    //     List<FriendChatInfoDto> friendChatInfos = friends.stream()
    //             .map(friend -> convertToFriendChatInfo(friend, user))
    //             .sorted((f1, f2) -> {
    //                 MemberStatus status1 = f1.status();
    //                 MemberStatus status2 = f2.status();
    //                 return compareStatus(status1, status2);
    //             })
    //             .collect(Collectors.toList());
        
    //     log.info("친구 목록 상태별 정렬 완료: userId={}, friendCount={}", userId, friendChatInfos.size());
    //     return friendChatInfos;
    // }

    // /**
    //  * 상태 우선순위에 따른 비교
    //  */
    // private int compareStatus(MemberStatus status1, MemberStatus status2) {
    //     // 상태별 우선순위 정의
    //     Map<MemberStatus, Integer> priority = Map.of(
    //             MemberStatus.ONLINE, 1,
    //             MemberStatus.IN_GAME, 2,
    //             MemberStatus.OFFLINE, 3);

    //     return priority.get(status1).compareTo(priority.get(status2));
    // }

    // /**
    //  * FriendChatInfoDto로 변환
    //  */
    // private FriendChatInfoDto convertToFriendChatInfo(Friend friend, Member user) {
    //     ChatRoom chatRoom = chatRoomService.findPersonalRoom(user, friend.getFriend())
    //             .orElse(null);
    //     ChatParticipant participant = chatParticipantService
    //             .getParticipant(chatRoom, user)
    //             .orElse(null);
    //     int unreadMessageCount = participant != null ? chatParticipantService.getUnreadMessageCount(participant) : 0;
    //     return FriendChatInfoDto.of(friend, chatRoom, participant, unreadMessageCount);
    // }

    // /**
    //  * 친구 요청 보내기 (최대 50명 제한)
    //  */
    // @Transactional
    // public FriendRequest sendFriendRequest(final UUID requesterId, final UUID receiverId) {
    //     // 요청자와 수신자 조회
    //     Map<UUID, Member> members = memberService.getMembersByIds(Arrays.asList(requesterId, receiverId));
    //     Member requester = members.get(requesterId);
    //     Member receiver = members.get(receiverId);

    //     // 수신자의 대기 중인 친구 요청 수 확인
    //     int pendingRequestCount = friendRequestService.countPendingRequests(receiver);
    //     if (pendingRequestCount > MAX_PENDING_REQUESTS) {
    //         throw new BadRequestException(ExceptionType.MAX_PENDING_REQUESTS_EXCEEDED);
    //     }
        
    //     // 친구 요청 생성
    //     return friendRequestService.createFriendRequest(requester, receiver);
    // }

    // /**
    //  * 친구 요청 수락
    //  */
    // @Transactional
    // public Friend acceptFriendRequest(final Long requestId) {
    //     return friendRequestService.acceptFriendRequest(requestId);
    // }

    // /**
    //  * 친구 요청 거절
    //  */
    // @Transactional
    // public void rejectFriendRequest(final Long requestId) {
    //     friendRequestService.rejectFriendRequest(requestId);
    // }

    // /**
    //  * 받은 친구 요청 목록 조회
    //  */
    // public List<FriendRequest> getReceivedFriendRequests(final UUID userId) {
    //     Member user = memberService.getOrThrow(userId);
    //     return friendRequestService.getReceivedRequests(user);
    // }

    // /**
    //  * 친구 차단하기
    //  * 
    //  * @param userId   차단을 요청한 사용자 ID
    //  * @param friendId 차단할 친구 ID
    //  */
    // @Transactional
    // public void blockFriend(final UUID userId, final UUID friendId) {
    //     // 사용자와 친구 정보 조회
    //     Map<UUID, Member> members = memberService.getMembersByIds(Arrays.asList(userId, friendId));
    //     Member user = members.get(userId);
    //     Member friend = members.get(friendId);

    //     // 친구 관계 확인
    //     Friend friendship = friendService.findFriendship(user, friend);
    //     if (friendship == null) {
    //         throw new BadRequestException(ExceptionType.NOT_FRIEND);
    //     }

    //     // 친구 차단
    //     friendService.blockFriend(friendship.getId());

    //     // 기존 채팅방이 있다면 찾기
    //     ChatRoom chatRoom = chatRoomService.findPersonalRoom(user, friend).orElse(null);
    //     if (chatRoom != null) {
    //         // 채팅방 비활성화
    //         chatRoomService.deactivateRoom(chatRoom.getId());
    //     }
    // }

    // /**
    //  * 친구 차단 해제하기
    //  * 
    //  * @param userId   차단 해제를 요청한 사용자 ID
    //  * @param friendId 차단 해제할 친구 ID
    //  */
    // @Transactional
    // public void unblockFriend(final UUID userId, final UUID friendId) {
    //     // 사용자와 친구 정보 조회
    //     Map<UUID, Member> members = memberService.getMembersByIds(Arrays.asList(userId, friendId));
    //     Member user = members.get(userId);
    //     Member friend = members.get(friendId);

    //     // 친구 관계 확인
    //     Friend friendship = friendService.findFriendship(user, friend);
    //     if (friendship == null) {
    //         throw new BadRequestException(ExceptionType.NOT_FRIEND);
    //     }

    //     // 친구 차단 해제
    //     friendService.unblockFriend(friendship.getId());
    // }

    // /**
    //  * 친구 메모 수정
    //  * @param userId   사용자 ID
    //  * @param friendId 메모를 수정할 친구 ID
    //  * @param memo     수정할 메모 내용
    //  */
    // @Transactional
    // public void updateFriendMemo(final UUID userId, final UUID friendId, final String memo) {
    //     // 사용자와 친구 정보 조회
    //     Map<UUID, Member> members = memberService.getMembersByIds(Arrays.asList(userId, friendId));
    //     Member user = members.get(userId);
    //     Member friend = members.get(friendId);

    //     // 친구 관계 확인
    //     Friend friendship = friendService.findFriendship(user, friend);
    //     if (friendship == null) {
    //         throw new BadRequestException(ExceptionType.NOT_FRIEND);
    //     }

    //     // 메모 수정
    //     friendService.updateMemo(friendship.getId(), memo);
    // }

    // /**
    //  * 게임 초대 전송
    //  * @param senderId    초대하는 사용자 ID
    //  * @param recipientId 초대받는 사용자 ID
    //  * @param gameType    게임 타입
    //  */
    // @Transactional
    // public void sendGameInvite(final UUID senderId, final UUID recipientId) {
    //     // 발신자와 수신자 조회
    //     Map<UUID, Member> members = memberService.getMembersByIds(Arrays.asList(senderId, recipientId));
    //     Member sender = members.get(senderId);
    //     Member recipient = members.get(recipientId);

    //     // 수신자가 오프라인인지 확인
    //     if (recipient.getStatus() == MemberStatus.OFFLINE) {
    //         throw new BadRequestException(ExceptionType.RECIPIENT_OFFLINE);
    //     }

    //     // 수신자가 이미 게임 중인지 확인
    //     if (recipient.getStatus() == MemberStatus.IN_GAME) {
    //         throw new BadRequestException(ExceptionType.RECIPIENT_IN_GAME);
    //     }

    //     // 게임 초대 요청 생성
    //     friendRequestService.createGameInviteRequest(sender, recipient);
    // }

    // /**
    //  * 게임 초대 요청 수락
    //  */
    // @Transactional
    // public void acceptGameInvite(final Long requestId) {
    //     friendRequestService.acceptGameInviteRequest(requestId);
    // }

    // /**
    //  * 게임 초대 요청 거절
    //  */
    // @Transactional
    // public void rejectGameInvite(final Long requestId) {
    //     friendRequestService.rejectGameInviteRequest(requestId);
    // }

    // /**
    //  * 친구 삭제
    //  * - 양방향 친구 관계 삭제
    //  * - 친구 폴더에서 제거
    //  * - 1:1 채팅방 비활성화 (선택적)
    //  */
    // @Transactional
    // public void deleteFriend(final UUID userId, final UUID friendId, final boolean deactivateChat) {
    //     // 사용자와 친구 정보 조회
    //     Map<UUID, Member> members = memberService.getMembersByIds(Arrays.asList(userId, friendId));
    //     Member user = members.get(userId);
    //     Member friend = members.get(friendId);
        
    //     // 친구 관계 확인
    //     if (!friendService.areFriends(user, friend)) {
    //         throw new BadRequestException(ExceptionType.NOT_FRIEND);
    //     }
        
    //     // 1. 친구 폴더에서 제거
    //     List<FriendInFolder> friendFolders = friendInFolderService.getFoldersByFriend(
    //         friendService.findFriendship(user, friend)
    //     );
    //     for (FriendInFolder folder : friendFolders) {
    //         friendInFolderService.removeFriendFromFolder(folder.getId());
    //     }
        
    //     // 2. 친구 관계 삭제
    //     friendService.deleteFriendship(user, friend);
        
    //     // 3. 채팅방 비활성화
    //     if (deactivateChat) {
    //         chatRoomService.findPersonalRoom(user, friend)
    //             .ifPresent(chatRoom -> chatRoomService.deactivateRoom(chatRoom.getId()));
    //     }
    // }

    // /**
    //  * 친구 목록 검색 (닉네임과 태그로 검색)
    //  * @param userId          사용자 ID
    //  * @param nicknameKeyword 닉네임 검색어 (선택)
    //  * @param tagKeyword      태그 검색어 (선택)
    //  * @return 검색된 친구 목록과 해당 친구와의 채팅방 정보
    //  */
    // public List<FriendChatInfoDto> searchFriends(final UUID userId, final String nicknameKeyword, final String tagKeyword) {
    //     Member user = memberService.getOrThrow(userId);

    //     // 두 검색어가 모두 null이면 예외 발생
    //     if (nicknameKeyword == null && tagKeyword == null) {
    //         throw new BadRequestException(ExceptionType.SEARCH_KEYWORD_REQUIRED);
    //     }

    //     List<Friend> searchResults = friendService.searchFriends(user, nicknameKeyword, tagKeyword);

    //     List<FriendChatInfoDto> friendChatInfos = searchResults.stream()
    //             .map(friend -> convertToFriendChatInfo(friend, user))
    //             .collect(Collectors.toList());

    //     return friendChatInfos;
    // }

    // // 친구 폴더 관련 기능들 보류

    // /**
    //  * 친구 폴더 생성
    //  * @param userId 사용자 ID
    //  * @param folderName 폴더 이름
    //  * @return 생성된 폴더
    //  */
    // @Transactional
    // public FriendFolder createFriendFolder(final UUID userId, final String folderName) {
    //     Member user = memberService.getOrThrow(userId);
    //     return friendFolderService.createFolder(user, folderName);
    // }

    // /**
    //  * 친구 폴더 이름 수정
    //  * @param userId        사용자 ID
    //  * @param folderId      폴더 ID
    //  * @param newFolderName 새로운 폴더 이름
    //  */
    // @Transactional
    // public void updateFriendFolderName(final UUID userId, final UUID folderId, final String newFolderName) {
    //     Member user = memberService.getOrThrow(userId);
    //     // 폴더 소유자 확인
    //     if (!friendFolderService.isOwner(folderId, user)) {
    //         throw new BadRequestException(ExceptionType.NOT_FOLDER_OWNER);
    //     }
    //     friendFolderService.updateFolderName(folderId, newFolderName);
    // }

    // /**
    //  * 친구 폴더 삭제
    //  * @param userId   사용자 ID
    //  * @param folderId 폴더 ID
    //  */
    // @Transactional
    // public void deleteFriendFolder(final UUID userId, final UUID folderId) {
    //     Member user = memberService.getOrThrow(userId);
    //     // 폴더 소유자 확인
    //     if (!friendFolderService.isOwner(folderId, user)) {
    //         throw new BadRequestException(ExceptionType.NOT_FOLDER_OWNER);
    //     }
    //     friendFolderService.deleteFolder(folderId);
    // }

    // /**
    //  * 폴더에서 친구 관리 해제
    //  * - 친구 관계는 유지되고 폴더에서만 제외됨
    //  * 
    //  * @param userId           사용자 ID
    //  * @param friendInFolderId 폴더 내 친구 ID
    //  */
    // @Transactional
    // public void unmanageFriendFromFolder(final UUID userId, final UUID friendInFolderId) {
    //     Member user = memberService.getOrThrow(userId);
    //     FriendInFolder friendInFolder = friendInFolderService.getOrThrow(friendInFolderId);

    //     // 폴더 소유자 확인
    //     if (!friendFolderService.isOwner(friendInFolder.getFolder().getId(), user)) {
    //         throw new BadRequestException(ExceptionType.NOT_FOLDER_OWNER);
    //     }

    //     // 폴더에서만 제거 (친구 관계는 유지)
    //     friendInFolderService.removeFriendFromFolder(friendInFolderId);
    // }

    // // 여기까지 친구 폴더 만들기 기능

    // /**
    //  * 닉네임으로 사용자 검색
    //  */
    // public List<UserSearchResponseDto> searchUsersByNickname(final UUID userId, final String nickname) {
    //     memberService.getOrThrow(userId);

    //     // 닉네임으로 사용자 검색 (자신 제외)
    //     List<Member> users = memberService.searchByNickname(nickname, userId);
        
    //     // // 현재 사용자의 친구 목록 조회
    //     // Set<UUID> friendIds = friendService.getFriends(currentUser).stream()
    //     //     .map(friend -> friend.getFriend().getId())
    //     //     .collect(Collectors.toSet());
        
    //     // // 차단한 사용자 목록 조회
    //     // Set<UUID> blockedIds = friendService.getBlockedFriends(currentUser).stream()
    //     //     .map(friend -> friend.getFriend().getId())
    //     //     .collect(Collectors.toSet());
            
    //     // // 친구 요청 보낸 목록 조회
    //     // Set<UUID> pendingRequestIds = friendRequestService.getPendingRequestsByRequester(currentUser).stream()
    //     //     .map(request -> request.getReceiver().getId())
    //     //     .collect(Collectors.toSet());

    //     return users.stream()
    //         .map(user -> new UserSearchResponseDto(
    //             user.getId(),
    //             user.getNickname()
    //         ))
    //         .collect(Collectors.toList());
    // }
}
