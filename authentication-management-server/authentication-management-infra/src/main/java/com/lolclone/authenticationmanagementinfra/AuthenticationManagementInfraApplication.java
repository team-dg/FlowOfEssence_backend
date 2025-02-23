package com.lolclone.authenticationmanagementinfra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "com.lolclone.authenticationmanagementinfra",
    "com.lolclone.commonmodule",
    "com.lolclone.authenticationmanagementdomain"
})
@EnableJpaRepositories(basePackages = "com.lolclone.authenticationmanagementdomain.repository")
@EntityScan(basePackages = "com.lolclone.authenticationmanagementdomain.domain")
@ConfigurationPropertiesScan(basePackages = "com.lolclone.authenticationmanagementdomain.domain.oauth2")
public class AuthenticationManagementInfraApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationManagementInfraApplication.class, args);
    }
}
