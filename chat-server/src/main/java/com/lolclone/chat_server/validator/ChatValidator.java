package com.lolclone.chat_server.validator;

import org.springframework.stereotype.Component;

import com.lolclone.chat_server.exception.common.BadRequestException;
import com.lolclone.chat_server.exception.common.NotFoundException;
import com.lolclone.chat_server.exception.domain.ExceptionType;
import com.lolclone.chat_server.domain.Message;

import java.util.List;

@Component
public final class ChatValidator {
    
    /**
     * 채팅 내역이 존재하는지 검증합니다.
     * @param messages 검증할 메시지 목록
     * @throws NotFoundException 채팅 내역이 없는 경우
     */
    public void validateChatHistoryExists(List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            throw new NotFoundException(ExceptionType.CHAT_HISTORY_NOT_FOUND);
        }
    }

    /**
     * 친구 요청의 발신자와 수신자 ID가 유효한지 검증합니다.
     * @param senderId 발신자 ID
     * @param receiverId 수신자 ID
     * @throws BadRequestException ID가 null이거나 발신자와 수신자가 동일한 경우
     */
    public void validateFriendRequest(Long senderId, Long receiverId) {
        if (senderId == null || receiverId == null) {
            throw new BadRequestException(ExceptionType.INVALID_REQUEST_ARGUMENT);
        }
        
        if (senderId.equals(receiverId)) {
            throw new BadRequestException(ExceptionType.INVALID_REQUEST_ARGUMENT);
        }
    }

    /**
     * 메시지 목록이 유효한지 검증합니다.
     * @param messages 검증할 메시지 목록
     * @throws BadRequestException 메시지 목록이 null인 경우
     */
    public void validateMessages(List<Message> messages) {
        if (messages == null) {
            throw new BadRequestException(ExceptionType.INVALID_MESSAGE);
        }
    }
}