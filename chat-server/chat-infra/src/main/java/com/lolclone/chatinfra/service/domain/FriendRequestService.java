package com.lolclone.chatinfra.service.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.friend.Friend;
import com.lolclone.chatdomain.domain.friendrequest.FriendRequest;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.exception.UnauthorizedFriendRequestException;
import com.lolclone.chatdomain.repository.FriendRequestRepository;
import com.lolclone.chatdomain.repository.MemberRepository;
import com.lolclone.chatdomain.repository.friend.FriendRepository;
import com.lolclone.chatinfra.exception.commonexception.BadRequestException;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;
import com.lolclone.chatserviceapi.dto.FriendRequestResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    // /**
    //  * 친구 요청을 생성합니다.
    //  */
    // public FriendRequestId createFriendRequest(MemberId requesterId, MemberId receiverId) {
    //     Member requester = findMemberById(requesterId);
    //     Member receiver = findMemberById(receiverId);

    //     validateFriendRequestCreation(requester, receiver);

    //     FriendRequest friendRequest = FriendRequest.create(requester, receiver);
    //     FriendRequest savedRequest = friendRequestRepository.save(friendRequest);

    //     // eventPublisher.publish(new FriendRequestCreatedEvent(
    //     //         savedRequest.getId(),
    //     //         requesterId,
    //     //         receiverId));

    //     return savedRequest.getId();
    // }

    // /**
    //  * 친구 요청을 수락합니다.
    //  */
    // public void acceptFriendRequest(MemberId receiverId, FriendRequestId requestId) {
    //     FriendRequest request = findFriendRequestById(requestId);
    //     Member receiver = findMemberById(receiverId);

    //     validateFriendRequestAcceptance(request, receiver);

    //     Friend newFriend = request.accept();
    //     friendRepository.save(newFriend);

    //     // eventPublisher.publish(new FriendRequestAcceptedEvent(
    //     //         request.getId(),
    //     //         request.getRequester().getId(),
    //     //         receiverId));
    // }

    // /**
    //  * 친구 요청을 거절합니다.
    //  */
    // public void rejectFriendRequest(MemberId receiverId, FriendRequestId requestId) {
    //     FriendRequest request = findFriendRequestById(requestId);
    //     Member receiver = findMemberById(receiverId);

    //     validateFriendRequestRejection(request, receiver);

    //     request.reject();
    //     friendRequestRepository.save(request);

    //     // eventPublisher.publish(new FriendRequestRejectedEvent(
    //     //         request.getId(),
    //     //         request.getRequester().getId(),
    //     //         receiverId));
    // }

    // /**
    //  * 받은 친구 요청 목록을 조회합니다.
    //  */
    // @Transactional(readOnly = true)
    // public List<FriendRequestResponse> getReceivedFriendRequests(MemberId memberId) {
    //     Member member = findMemberById(memberId);
    //     return friendRequestRepository.findPendingRequestsByReceiver(member)
    //             .stream()
    //             .map(FriendRequestResponse::from)
    //             .collect(Collectors.toList());
    // }

    // // 검증 메서드
    // private void validateFriendRequestCreation(Member requester, Member receiver) {
    //     if (friendRepository.existsByUsers(requester, receiver)) {
    //         throw new BadRequestException(ExceptionType.ALREADY_FRIENDS);
    //     }
        
    //     if (friendRequestRepository.existsPendingRequest(requester, receiver)) {
    //         throw new BadRequestException(ExceptionType.FRIEND_REQUEST_ALREADY_SENT);
    //     }
    // }

    // private void validateFriendRequestAcceptance(FriendRequest request, Member receiver) {
    //     if (!request.getReceiver().equals(receiver)) {
    //         throw new UnauthorizedFriendRequestException("친구 요청을 수락할 권한이 없습니다.", request.getId());
    //     }
    // }

    // private void validateFriendRequestRejection(FriendRequest request, Member receiver) {
    //     if (!request.getReceiver().equals(receiver)) {
    //         throw new UnauthorizedFriendRequestException("친구 요청을 거절할 권한이 없습니다.", request.getId());
    //     }
    // }

    // // 헬퍼 메서드
    // private Member findMemberById(MemberId memberId) {
    //     return memberRepository.findById(memberId)
    //     .orElseThrow(() -> new NotFoundException(ExceptionType.MEMBER_NOT_FOUND));
    // }

    // private FriendRequest findFriendRequestById(FriendRequestId requestId) {
    //     return friendRequestRepository.findById(requestId)
    //     .orElseThrow(() -> new NotFoundException(ExceptionType.FRIEND_REQUEST_NOT_FOUND));
    // }
}
