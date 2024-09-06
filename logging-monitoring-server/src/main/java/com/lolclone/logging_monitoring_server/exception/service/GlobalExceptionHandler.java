package com.lolclone.logging_monitoring_server.exception.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lolclone.logging_monitoring_server.exception.domain.BusinessException;
import com.lolclone.logging_monitoring_server.exception.domain.ExceptionType;
import com.lolclone.logging_monitoring_server.exception.dto.ExceptionResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity.status(e.getStatus())
                .body(new ExceptionResponse(e.getCode(), e.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);// 처리되지 않은 모든 오류
        return ResponseEntity
                .status(ExceptionType.EXCEPTION.getStatus())
                .body(new ExceptionResponse(ExceptionType.EXCEPTION.getCode(), ExceptionType.EXCEPTION.getMessage()));
    }
}
