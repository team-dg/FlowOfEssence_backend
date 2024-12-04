package com.lolclone.chat_server.exception.domain;

import static org.springframework.http.HttpStatus.*;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionType {
    // 400 Bad Request
    INVALID_REQUEST_ARGUMENT(BAD_REQUEST, "E001", "잘못된 요청입니다."),
    INVALID_MESSAGE(BAD_REQUEST, "C001", "메시지가 유효하지 않습니다."),
    INVALID_TEAM_ID(BAD_REQUEST, "T002", "유효하지 않은 팀 ID입니다."),
    INVALID_GAME_ID(BAD_REQUEST, "T003", "유효하지 않은 게임 ID입니다."),
    INVALID_MESSAGE_LENGTH(BAD_REQUEST, "P001", "메시지는 1자 이상 500자 이하여야 합니다."),
    INVALID_RECEIVER(BAD_REQUEST, "P002", "유효하지 않은 수신자입니다."),
    NOT_FRIEND(BAD_REQUEST, "P003", "친구 관계가 아닌 사용자와는 대화할 수 없습니다."),
    DUPLICATE_FOLDER_NAME(BAD_REQUEST, "P004", "이미 존재하는 폴더 이름입니다."),

    // 401 Unauthorized
    UNAUTHORIZED_TEAM_ACCESS(UNAUTHORIZED, "T001", "팀 채팅방에 접근할 권한이 없습니다."),
    UNAUTHORIZED_ACCESS(UNAUTHORIZED, "T005", "해당 팀의 채팅방에 접근 권한이 없습니다."),
    
    // 404 Not Found
    TEAM_ROOM_NOT_FOUND(NOT_FOUND, "T004", "팀 채팅방을 찾을 수 없습니다."),
    CHAT_HISTORY_NOT_FOUND(NOT_FOUND, "P004", "채팅 내역을 찾을 수 없습니다."),
    USER_NOT_FOUND(NOT_FOUND, "P004", "유저를 찾을 수 없습니다."),
    FRIEND_NOT_FOUND(NOT_FOUND, "P006", "친구 관계를 찾을 수 없습니다."),
    FOLDER_NOT_FOUND(NOT_FOUND, "P007", "폴더를 찾을 수 없습니다."),

    // 403 Forbidden
    BLOCKED_USER(FORBIDDEN, "P007", "차단된 사용자에게는 초대를 보낼 수 없습니다."),
    
    // 500 Internal Server Error
    EXCEPTION(INTERNAL_SERVER_ERROR, "E000", "예상치 못한 오류가 발생했습니다."),
    MESSAGING_ERROR(INTERNAL_SERVER_ERROR, "C002", "메시지 전송 중 오류가 발생했습니다."),
    DATABASE_ERROR(INTERNAL_SERVER_ERROR, "C003", "데이터베이스 작업 중 오류가 발생했습니다."),
    WEBSOCKET_CONNECTION_ERROR(INTERNAL_SERVER_ERROR, "W001", "웹소켓 연결 중 오류가 발생했습니다."),
    WEBSOCKET_MESSAGE_ERROR(INTERNAL_SERVER_ERROR, "W002", "웹소켓 메시지 처리 중 오류가 발생했습니다."),
    PRIVATE_MESSAGE_SEND_ERROR(INTERNAL_SERVER_ERROR, "P005", "개인 메시지 전송 중 오류가 발생했습니다."),
    ;
    
    private final HttpStatus status;
    private final String code;
    private final String message;
}