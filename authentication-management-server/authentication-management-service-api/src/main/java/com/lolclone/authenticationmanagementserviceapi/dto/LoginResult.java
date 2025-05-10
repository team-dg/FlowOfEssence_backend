package com.lolclone.authenticationmanagementserviceapi.dto;

import java.util.UUID;

public record LoginResult(
    UUID userId,
    String email
) {
    
}
