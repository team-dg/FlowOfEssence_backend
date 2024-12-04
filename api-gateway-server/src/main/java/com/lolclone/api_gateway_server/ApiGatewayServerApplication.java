package com.lolclone.api_gateway_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.lolclone.api_gateway_server",
    "com.lolclone.common_module"
})
public class ApiGatewayServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayServerApplication.class, args);
    }
}
