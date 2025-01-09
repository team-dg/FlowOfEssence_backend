package com.lolclone.chatinfra.handler;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;

import com.lolclone.chatdomain.domain.Message;
import com.lolclone.chatinfra.exception.commonexception.InternalServerException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;
import com.lolclone.chatserviceapi.dto.MessageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MessageHandler {
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    public void sendMessage(final Message message) {
        try {
            MessageDto messageDto = MessageDto.from(message);

            switch (message.getMessageType()) {
                case TEXT -> messagingTemplate.convertAndSendToUser(
                        message.getRecipient().getId().toString(),
                        "/queue/messages",
                        messageDto
                    );
                case SYSTEM -> messagingTemplate.convertAndSend(
                        "/topic/system-messages",
                        messageDto
                    );
                case WHISPER -> messagingTemplate.convertAndSendToUser(
                        message.getRecipient().getId().toString(),
                        "/queue/messages",
                        messageDto
                    );
            }
        } catch (Exception e) {
            // 실패 처리 로직 (재시도 큐에 넣기 등)
            throw new InternalServerException(ExceptionType.PRIVATE_MESSAGE_SEND_ERROR);
        }
    }
}
