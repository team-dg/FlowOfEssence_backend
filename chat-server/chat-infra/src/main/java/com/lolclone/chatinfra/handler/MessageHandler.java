package com.lolclone.chatinfra.handler;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lolclone.chatdomain.domain.message.Message;
import com.lolclone.chatdomain.domain.message.MessageType;
import com.lolclone.chatinfra.exception.commonexception.InternalServerException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;
import com.lolclone.chatserviceapi.dto.MessageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    public void sendMessage(final Message message) {
        // try {
        //     MessageDto messageDto = MessageDto.from(message);
        //     MessageType type = message.getMessageType();

        //     if (type.isText()) {
        //         messagingTemplate.convertAndSendToUser(
        //                 message.getRecipient().getId().toString(),
        //                 "/queue/messages",
        //                 messageDto);
        //     } else if (type.isSystem()) {
        //         messagingTemplate.convertAndSend(
        //                 "/topic/system-messages",
        //                 messageDto);
        //     } else if (type.isWhisper()) {
        //         messagingTemplate.convertAndSendToUser(
        //                 message.getRecipient().getId().toString(),
        //                 "/queue/messages",
        //                 messageDto);
        //     }
        // } catch (Exception e) {
        //     // 실패 처리 로직 (재시도 큐에 넣기 등)
        //     throw new InternalServerException(ExceptionType.PRIVATE_MESSAGE_SEND_ERROR);
        // }
    }
}
