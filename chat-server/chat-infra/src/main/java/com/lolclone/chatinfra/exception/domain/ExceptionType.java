package com.lolclone.chatinfra.exception.domain;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionType {
    //400
    INVALID_REQUEST_ARGUMENT(HttpStatus.BAD_REQUEST, "E001", "잘못된 요청입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "E002", "존재하지 않는 사용자입니다."),
    MESSAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "E003", "존재하지 않는 메시지입니다."),
    CHAT_PARTICIPANT_NOT_FOUND(HttpStatus.BAD_REQUEST, "E004", "존재하지 않는 채팅 참가자입니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "E005", "존재하지 않는 채팅방입니다."),
    FRIEND_FOLDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "E006", "존재하지 않는 친구 폴더입니다."),
    FRIEND_IN_FOLDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "E007", "존재하지 않는 친구 폴더 친구입니다."),
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST, "E008", "존재하지 않는 친구 요청입니다."),
    FRIEND_REQUEST_ALREADY_SENT(HttpStatus.BAD_REQUEST, "E009", "이미 친구 요청을 보냈습니다."),
    FRIEND_NOT_FOUND(HttpStatus.BAD_REQUEST, "E010", "존재하지 않는 친구입니다."),
    REPORT_NOT_FOUND(HttpStatus.BAD_REQUEST, "E011", "존재하지 않는 신고입니다."),
    SELF_REPORT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "E012", "자신을 신고할 수 없습니다."),
    NOT_FRIEND(HttpStatus.BAD_REQUEST, "E013", "친구가 아닙니다."),
    NOT_PERSONAL_CHAT_ROOM(HttpStatus.BAD_REQUEST, "E014", "개인 채팅방이 아닙니다."),
    NOT_CHAT_PARTICIPANT(HttpStatus.BAD_REQUEST, "E015", "채팅방 참여자가 아닙니다."),
    RECIPIENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "E016", "수신자를 찾을 수 없습니다."),
    BLOCKED_BY_USER(HttpStatus.BAD_REQUEST, "E017", "상대방이 나를 차단했습니다."),
    BLOCKED_USER(HttpStatus.BAD_REQUEST, "E018", "본인이 상대방을 차단했습니다."),
    NOTIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "E019", "존재하지 않는 알림입니다."),
    MAX_PENDING_REQUESTS_EXCEEDED(HttpStatus.BAD_REQUEST, "E020", "최대 대기 요청 수를 초과했습니다."),
    ALREADY_FRIENDS(HttpStatus.BAD_REQUEST, "E021", "이미 친구입니다."),
    RECIPIENT_OFFLINE(HttpStatus.BAD_REQUEST, "E022", "수신자가 오프라인입니다."),
    RECIPIENT_IN_GAME(HttpStatus.BAD_REQUEST, "E023", "수신자가 게임 중입니다."),
    SEARCH_KEYWORD_REQUIRED(HttpStatus.BAD_REQUEST, "E024", "검색어가 필요합니다."),
    NOT_FOLDER_OWNER(HttpStatus.BAD_REQUEST, "E025", "폴더 소유자가 아닙니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "E026", "존재하지 않는 사용자입니다."),
    GAME_INVITE_NOT_FOUND(HttpStatus.BAD_REQUEST, "E027", "존재하지 않는 게임 초대입니다."),
    GAME_INVITE_ALREADY_SENT(HttpStatus.BAD_REQUEST, "E028", "이미 게임 초대가 존재합니다."),
    CHATROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "E029", "존재하지 않는 채팅방입니다."),

    //500
    EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "E003", "예상치 못한 오류가 발생했습니다."),
    PRIVATE_MESSAGE_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E004", "메시지 전송에 실패했습니다."),
    NOTIFICATION_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E005", "알림 전송에 실패했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
