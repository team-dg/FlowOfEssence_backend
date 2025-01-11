package com.lolclone.chatinfra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "com.lolclone.chatinfra",
    "com.lolclone.commonmodule",
    "io.eventuate.tram"
})
@EnableJpaRepositories(basePackages = "com.lolclone.chatdomain.repository")
@EntityScan(basePackages = "com.lolclone.chatdomain.domain")
public class ChatInfraApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatInfraApplication.class, args);
    }

}
