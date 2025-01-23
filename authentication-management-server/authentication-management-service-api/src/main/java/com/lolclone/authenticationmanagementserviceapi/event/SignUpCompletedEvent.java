package com.lolclone.authenticationmanagementserviceapi.event;

import java.util.UUID;

public record SignUpCompletedEvent(
    UUID userId
) {
    
}
