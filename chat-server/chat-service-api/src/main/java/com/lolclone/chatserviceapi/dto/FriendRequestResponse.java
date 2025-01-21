package com.lolclone.chatserviceapi.dto;

import java.time.LocalDateTime;

import com.lolclone.chatdomain.domain.friendrequest.FriendRequestId;
import com.lolclone.chatdomain.domain.friendrequest.FriendRequestStatus;
import com.lolclone.chatdomain.domain.member.MemberId;

import lombok.Getter;

@Getter
public class FriendRequestResponse {
    private FriendRequestId requestId;
    private MemberId requesterId;
    private String requesterNickname;
    private LocalDateTime requestedAt;
    private FriendRequestStatus status;

    // private FriendRequestResponse(FriendRequest request) {
    //     this.requestId = request.getId();
    //     this.requesterId = request.getRequester().getId();
    //     this.requesterNickname = request.getRequester().getNickname().getValue();
    //     this.requestedAt = request.getCreatedDate();
    //     this.status = request.getStatus();
    // }

    // public static FriendRequestResponse from(FriendRequest request) {
    //     return new FriendRequestResponse(request);
    // }
}
