package com.lolclone.authenticationmanagementserviceapi.dto;

import java.util.UUID;

/**
 * 외부 서비스에 전송하기 위한 회원 정보 DTO
 */
public record MemberDTO(
    UUID memberId,
    String nickname
) {

}
