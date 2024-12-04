package com.lolclone.authentication_management_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.lolclone.authentication_management_server",
		"com.lolclone.common_module"
})
public class AuthenticationManagementServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationManagementServerApplication.class, args);
	}

}
