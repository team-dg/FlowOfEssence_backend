package com.lolclone.chat_server.dto.response;

import com.lolclone.chat_server.domain.FriendFolder;

public record FriendFolderResponseDto(
    Long folderId,
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
