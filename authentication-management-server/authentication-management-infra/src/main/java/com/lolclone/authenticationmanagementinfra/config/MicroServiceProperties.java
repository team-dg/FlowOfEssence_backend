package com.lolclone.authenticationmanagementinfra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Microservice URL configuration properties. ⚙️
 */
@ConfigurationProperties(prefix = "microservice")
public record MicroServiceProperties(
  String chatServiceUrl,
  String matchingServiceUrl
) {}