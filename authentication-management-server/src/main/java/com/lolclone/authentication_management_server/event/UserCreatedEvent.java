package com.lolclone.authentication_management_server.event;

import com.lolclone.authentication_management_server.domain.entity.Member;

public record UserCreatedEvent(
    Member member
) {
    
}
