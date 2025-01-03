package com.lolclone.chatdomain.exception;

import com.lolclone.chatdomain.domain.MemberState;

public class UnsupportedStateTransitionException extends RuntimeException {
    public UnsupportedStateTransitionException(MemberState state) {
        super("current state: " + state);
    }
}
