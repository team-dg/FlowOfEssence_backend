package com.lolclone.authentication_management_server.dto;

public record LoginRequest(
    String username,
    String password
) {

}
