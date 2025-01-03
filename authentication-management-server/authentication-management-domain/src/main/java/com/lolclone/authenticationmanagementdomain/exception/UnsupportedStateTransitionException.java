package com.lolclone.authenticationmanagementdomain.exception;

import com.lolclone.authenticationmanagementdomain.domain.UserStatus;

public class UnsupportedStateTransitionException extends RuntimeException {
    public UnsupportedStateTransitionException(final UserStatus status) {
        super(String.format("Unsupported state transition: %s", status));
    }
}
