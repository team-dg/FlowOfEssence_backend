package com.lolclone.chatdomain.repository.friend.query;

import java.util.UUID;

import com.querydsl.core.annotations.QueryProjection;

public record FriendChatInfoDto(
    UUID friendId,
    String friendNickname,
    FriendStateDto friendState,
    ChatRoomInfoDto chatRoomInfo,
    FriendRelationDto friendRelation
) {
    @QueryProjection
    public FriendChatInfoDto(UUID friendId, String friendNickname, FriendStateDto friendState, ChatRoomInfoDto chatRoomInfo, FriendRelationDto friendRelation) {
        this.friendId = friendId;
        this.friendNickname = friendNickname;
        this.friendState = friendState;
        this.chatRoomInfo = chatRoomInfo;
        this.friendRelation = friendRelation;
    }
}
