package com.lolclone.userinfra.exception.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// import com.lolclone.userserviceapi.exception.commonexception.UserException;

// import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

// import com.lolclone.commonmodule.apigatewayserver.domain.AuthenticateContext;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger("ErrorLogger");
    private static final String LOG_FORMAT_INFO = "\n[🔵INFO] - ({} {})\n(id: {}, role: {})\n{}\n {}: {}";
    private static final String LOG_FORMAT_WARN = "\n[🟠WARN] - ({} {})\n(id: {}, role: {})";
    private static final String LOG_FORMAT_ERROR = "\n[🔴ERROR] - ({} {})\n(id: {}, role: {})";
    //private final AuthenticateContext authenticateContext;

    
    // private void logInfo(UserException e, HttpServletRequest request) {
    //     log.info(LOG_FORMAT_INFO, request.getMethod(), request.getRequestURI(),
    //             authenticateContext.getId(), authenticateContext.getRole(), e.getExceptionType(), e.getClass().getName(), e.getMessage());
    // }

    // private void logWarn(UserException e, HttpServletRequest request) {
    //     log.warn(LOG_FORMAT_WARN, request.getMethod(), request.getRequestURI(),
    //             authenticateContext.getId(), authenticateContext.getRole(), e);
    // }

    // private void logError(Exception e, HttpServletRequest request) {
    //     log.error(LOG_FORMAT_ERROR, request.getMethod(), request.getRequestURI(),
    //             authenticateContext.getId(), authenticateContext.getRole(), e);
    // }
}
