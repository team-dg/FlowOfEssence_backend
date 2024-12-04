package com.lolclone.chat_server.domain;

public enum UserStatus {
    ONLINE, 
    OFFLINE, 
    IN_GAME
    ;
    
    public String toLowerCase() {
        return this.name().toLowerCase();
    }
} 