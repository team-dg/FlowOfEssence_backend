package com.lolclone.chatserviceapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lolclone.chatdomain.domain.friendrequest.FriendRequestStatus;

import lombok.Getter;

@Getter
public class FriendRequestResponse {
    private UUID requestId;
    private UUID requesterId;
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
