package com.lolclone.userserviceapi.service;

import java.util.UUID;

public interface UserService {
    void createUser(UUID userId, String nickname);
    void undoCreateUser(UUID userId);
}
