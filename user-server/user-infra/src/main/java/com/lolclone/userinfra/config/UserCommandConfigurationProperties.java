package com.lolclone.userinfra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "command")
public class UserCommandConfigurationProperties {
    private String commandChannel = "userService";
    private String replyChannel = "authenticationService";

    public String getCommandChannel() {
        return commandChannel;
    }

    public void setCommandChannel(String commandChannel) {
        this.commandChannel = commandChannel;
    }

    public String getReplyChannel() {
        return replyChannel;
    }

    public void setReplyChannel(String replyChannel) {
        this.replyChannel = replyChannel;
    }
}
