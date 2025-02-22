package com.lolclone.chatserviceapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lolclone.chatdomain.domain.gameinvite.GameInvite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GameInviteDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private UUID inviterId;
        private UUID inviteeId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private UUID id;
        private UUID inviterId;
        private String inviterNickname;
        private UUID inviteeId;
        private String inviteeNickname;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime expiresAt;

        // public static Response from(GameInvite gameInvite) {
        //     return Response.builder()
        //         .id(gameInvite.getId().getValue())
        //         .inviterId(gameInvite.getInviter().getId().getValue())
        //         .inviterNickname(gameInvite.getInviter().getNickname().getValue())
        //         .inviteeId(gameInvite.getInvitee().getId().getValue())
        //         .inviteeNickname(gameInvite.getInvitee().getNickname().getValue())
        //         .status(gameInvite.getStatus().getStatus().getDescription())
        //         .createdAt(gameInvite.getCreatedDate())
        //         .expiresAt(gameInvite.getMetadata().getExpiresAt())
        //         .build();
        // }
    }
}
