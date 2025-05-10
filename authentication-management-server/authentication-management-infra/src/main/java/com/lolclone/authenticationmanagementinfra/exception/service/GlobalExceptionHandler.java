package com.lolclone.authenticationmanagementinfra.exception.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lolclone.authenticationmanagementinfra.exception.commonexception.AuthenticationException;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.BadRequestException;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.ForbiddenException;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.InternalServerException;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.NotFoundException;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.UnauthorizedException;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.UnexpectedException;
import com.lolclone.authenticationmanagementinfra.exception.commonexception.ValidException;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.exception.dto.ExceptionResponse;
import com.lolclone.authenticationmanagementinfra.exception.dto.ValidErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger("ErrorLogger");
    
    // 정적 상수로 StringBuilder를 미리 생성하는 대신 메서드에서 필요할 때 생성
    private static final String LOG_FORMAT_INFO_PATTERN = "[🔵INFO] - (%s %s)\nExceptionType: %s\n %s: %s";
    private static final String LOG_FORMAT_WARN_PATTERN = "[🟠WARN] - (%s %s)\nExceptionType: %s\n %s: %s";
    private static final String LOG_FORMAT_ERROR_PATTERN = "[🔴ERROR] - (%s %s)\nExceptionType: %s\n %s: %s";

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
        log.info("[🔵INFO] - Validation Error\n{}", ValidErrorResponse.from(e));
        return ResponseEntity.status(BAD_REQUEST).body(ValidErrorResponse.from(e));
    }

    private void logInfo(AuthenticationException e, HttpServletRequest request) {
        log.info(String.format(LOG_FORMAT_INFO_PATTERN, 
                request.getMethod(), 
                request.getRequestURI(),
                e.getExceptionType(), 
                e.getClass().getName(), 
                e.getMessage()));
    }

    private void logWarn(AuthenticationException e, HttpServletRequest request) {
        log.warn(String.format(LOG_FORMAT_WARN_PATTERN, 
                request.getMethod(), 
                request.getRequestURI(),
                e.getExceptionType(), 
                e.getClass().getName(), 
                e.getMessage()));
    }

    private void logError(Exception e, HttpServletRequest request) {
        log.error(String.format(LOG_FORMAT_ERROR_PATTERN, 
                request.getMethod(), 
                request.getRequestURI(),
                e.getClass().getName(), 
                e.getMessage()));
    }
}
