package com.lolclone.authentication_management_server.event;

import com.lolclone.database_server.authenticationserver.domain.Member;

public record UserDeletedEvent(
    Member member
) {
    
}
