package com.lolclone.api_gateway_server.common.exception.service;

import com.lolclone.api_gateway_server.common.exception.commonexception.ApiGateWayException;
import com.lolclone.api_gateway_server.common.exception.commonexception.BadRequestException;
import com.lolclone.api_gateway_server.common.exception.dto.ExceptionResponse;
import com.lolclone.api_gateway_server.domain.ReactiveAuthenticateContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@RequiredArgsConstructor
public class ReactiveGlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger("ErrorLogger");
    private static final String LOG_FORMAT_INFO = "\n[🔵INFO] - ({} {})\n(id: {}, role: {})\n{}\n {}: {}";
    private static final String LOG_FORMAT_WARN = "\n[🟠WARN] - ({} {})\n(id: {}, role: {})";
    private static final String LOG_FORMAT_ERROR = "\n[🔴ERROR] - ({} {})\n(id: {}, role: {})";
    private final ReactiveAuthenticateContext authenticateContext;

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<ExceptionResponse>> handleBadRequestException(BadRequestException e, ServerWebExchange exchange) {
        return logInfo(e, exchange)
                .then(Mono.just(ResponseEntity.status(e.getExceptionType().getStatus())
                        .body(ExceptionResponse.from(e))));
    }

    private Mono<Void> logInfo(ApiGateWayException e, ServerWebExchange exchange) {
        return Mono.zip(
                authenticateContext.getId(exchange),
                authenticateContext.getRole(exchange)
        ).doOnNext(tuple ->
                log.info(LOG_FORMAT_INFO,
                        exchange.getRequest().getMethod(),
                        exchange.getRequest().getURI(),
                        tuple.getT1(),
                        tuple.getT2(),
                        e.getExceptionType(),
                        e.getClass().getName(),
                        e.getMessage())
        ).then();
    }
}
