package com.lolclone.userdomain.exception;

import com.lolclone.userdomain.entity.MemberState;

public class UnsupportedStateTransitionException extends RuntimeException{
    public UnsupportedStateTransitionException(MemberState state) {
        super("current state: " + state);
    }
}
