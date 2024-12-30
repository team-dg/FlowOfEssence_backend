package com.lolclone.authenticationmanagementinfra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "com.lolclone.authenticationmanagementinfra",
    "com.lolclone.commonmodule",
    "io.eventuate.tram"
})
@EnableJpaRepositories(basePackages = "com.lolclone.authenticationmanagementdomain.repository")
@EntityScan(basePackages = "com.lolclone.authenticationmanagementdomain.domain")
public class AuthenticationManagementInfraApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationManagementInfraApplication.class, args);
    }
}
