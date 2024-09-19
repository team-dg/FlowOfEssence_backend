package com.lolclone.common_module.exception.service;

import com.lolclone.common_module.authenticationserver.AuthenticateContext;
import com.lolclone.common_module.commonexception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lolclone.common_module.exception.domain.ExceptionType;
import com.lolclone.common_module.exception.dto.ExceptionResponse;
import com.lolclone.common_module.exception.dto.ValidErrorResponse;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger("ErrorLogger");
    private static final String LOG_FORMAT_INFO = "\n[🔵INFO] - ({} {})\n(id: {}, role: {})\n{}\n {}: {}";
    private static final String LOG_FORMAT_WARN = "\n[🟠WARN] - ({} {})\n(id: {}, role: {})";
    private static final String LOG_FORMAT_ERROR = "\n[🔴ERROR] - ({} {})\n(id: {}, role: {})";
    private final AuthenticateContext authenticateContext;

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException e, HttpServletRequest request) {
        logInfo(e, request);
        return ResponseEntity.status(e.getExceptionType().getStatus()).body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleForbiddenException(ForbiddenException e, HttpServletRequest request) {
        logInfo(e, request);
        return ResponseEntity.status(e.getExceptionType().getStatus()).body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ExceptionResponse> handleForbiddenException(InternalServerException e, HttpServletRequest request) {
        logWarn(e, request);
        return ResponseEntity.status(e.getExceptionType().getStatus()).body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e, HttpServletRequest request) {
        logInfo(e, request);
        return ResponseEntity.status(e.getExceptionType().getStatus()).body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException e, HttpServletRequest request) {
        logInfo(e, request);
        return ResponseEntity.status(e.getExceptionType().getStatus()).body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(UnexpectedException.class)
    public ResponseEntity<ExceptionResponse> handleUnexpectedException(UnexpectedException e, HttpServletRequest request) {
        logInfo(e, request);
        return ResponseEntity.status(e.getExceptionType().getStatus()).body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(ValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidException(ValidException e, HttpServletRequest request) {
        logInfo(e, request);
        return ResponseEntity.status(e.getExceptionType().getStatus()).body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e, HttpServletRequest request) {
        logError(e, request);
        return ResponseEntity
                .status(ExceptionType.EXCEPTION.getStatus())
                .body(new ExceptionResponse(ExceptionType.EXCEPTION.getCode(), ExceptionType.EXCEPTION.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException e,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidErrorResponse.from(e));
    }

    private void logInfo(FlowOfEssenceException e, HttpServletRequest request) {
        log.info(LOG_FORMAT_INFO, request.getMethod(), request.getRequestURI(),
                authenticateContext.getId(), authenticateContext.getRole(), e.getExceptionType(), e.getClass().getName(), e.getMessage());
    }

    private void logWarn(FlowOfEssenceException e, HttpServletRequest request) {
        log.warn(LOG_FORMAT_WARN, request.getMethod(), request.getRequestURI(),
                authenticateContext.getId(), authenticateContext.getRole(), e);
    }

    private void logError(Exception e, HttpServletRequest request) {
        log.error(LOG_FORMAT_ERROR, request.getMethod(), request.getRequestURI(),
                authenticateContext.getId(), authenticateContext.getRole(), e);
    }
}
