package com.lolclone.chat_server.dto.response;

import java.util.UUID;

import com.lolclone.chat_server.domain.FriendFolder;

public record FriendFolderResponseDto(
    UUID folderId,
    String folderName,
    int friendCount
) {
    public static FriendFolderResponseDto from(FriendFolder folder) {
        return new FriendFolderResponseDto(
            folder.getId(),
            folder.getName(),
            folder.getFriends().size()
        );
    }
}
