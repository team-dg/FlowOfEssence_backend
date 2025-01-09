package com.lolclone.chatinfra.service.domain;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.Friend;
import com.lolclone.chatdomain.domain.FriendRequest;
import com.lolclone.chatdomain.domain.FriendStatus;
import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatdomain.repository.FriendRequestRepository;
import com.lolclone.chatinfra.exception.commonexception.BadRequestException;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final NotificationService notificationService;
    private final FriendService friendService;

    public FriendRequest getOrThrow(final Long id) {
        return friendRequestRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.FRIEND_REQUEST_NOT_FOUND));
    }

    /**
     * 친구 요청 생성
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public FriendRequest createFriendRequest(final Member requester, final Member receiver) {
        validateNotAlreadyRequested(requester, receiver);
        FriendRequest request = FriendRequest.create(requester, receiver);
        notificationService.sendFriendRequestNotification(request);
        return friendRequestRepository.save(request);
    }

    /**
     * 친구 요청 수락
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public Friend acceptFriendRequest(final Long requestId) {
        FriendRequest request = getOrThrow(requestId);
        // 친구 관계 생성
        Friend friendShip = friendService.createFriendship(request.getRequester(), request.getReceiver());
        request.accept();
        notificationService.sendFriendRequestAcceptedNotification(request);
        return friendShip;
    }

    /**
     * 친구 요청 거절
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void rejectFriendRequest(final Long requestId) {
        FriendRequest request = getOrThrow(requestId);
        request.reject();
        notificationService.sendFriendRequestRejectedNotification(request);
    }

    /**
     * 게임 초대 요청 생성
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public FriendRequest createGameInviteRequest(final Member requester, final Member receiver) {
        FriendRequest request = FriendRequest.create(requester, receiver);
        notificationService.sendGameInviteNotification(request);
        return friendRequestRepository.save(request);
    }

    /**
     * 게임 초대 요청 수락
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public Friend acceptGameInviteRequest(final Long requestId) {
        FriendRequest request = getOrThrow(requestId);
        // TODO: 게임 방 참가 로직 작성
        request.acceptGameInvite();
        notificationService.sendGameInviteAcceptedNotification(request);
        return null;
    }

    /**
     * 게임 초대 요청 거절
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void rejectGameInviteRequest(final Long requestId) {
        FriendRequest request = getOrThrow(requestId);
        request.rejectGameInvite();
        notificationService.sendGameInviteRejectedNotification(request);
    }

    /**
     * 받은 친구 요청 목록 조회
     */
    public List<FriendRequest> getReceivedRequests(final Member receiver) {
        return friendRequestRepository.findByReceiverAndStatus(receiver, FriendStatus.PENDING);
    }

    /**
     * 보낸 친구 요청 목록 조회
     */
    public List<FriendRequest> getSentRequests(final Member requester) {
        return friendRequestRepository.findByRequesterAndStatus(requester, FriendStatus.PENDING);
    }

    /**
     * 친구 요청이 대기 상태인지 확인
     */
    public boolean isPending(final Long requestId) {
        FriendRequest request = getOrThrow(requestId);
        return request.isPending();
    }

    /**
     * 이미 친구 요청을 보냈는지 확인
     */
    private void validateNotAlreadyRequested(final Member requester, final Member receiver) {
        friendRequestRepository.findByRequesterAndReceiverAndStatus(requester, receiver, FriendStatus.PENDING)
                .ifPresent(request -> {
                    throw new BadRequestException(ExceptionType.FRIEND_REQUEST_ALREADY_SENT);
                });
    }

    /**
     * 대기 중인 친구 요청 수 조회
     */
    public int countPendingRequests(final Member receiver) {
        return friendRequestRepository.countByReceiverAndStatus(receiver, FriendStatus.PENDING);
    }
}
