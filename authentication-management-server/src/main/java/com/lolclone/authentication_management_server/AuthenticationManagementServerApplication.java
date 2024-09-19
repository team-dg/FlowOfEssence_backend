package com.lolclone.authentication_management_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.lolclone.database_server.authenticationserver.domain"})
@EnableJpaRepositories(basePackages = {"com.lolclone.database_server"})
@ComponentScan(basePackages = {"com.lolclone.authentication_management_server", "com.lolclone.common_module"})
public class AuthenticationManagementServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationManagementServerApplication.class, args);
	}

}
