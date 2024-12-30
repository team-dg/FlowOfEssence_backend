package com.lolclone.userinfra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
	"com.lolclone.userinfra",
	"com.lolclone.commonmodule",
	"io.eventuate.tram"
})
@EntityScan(basePackages = {
	"com.lolclone.userdomain.entity"
})
@EnableJpaRepositories(basePackages = {
	"com.lolclone.userdomain.repository"
})
public class UserInfraApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserInfraApplication.class, args);
	}

}
