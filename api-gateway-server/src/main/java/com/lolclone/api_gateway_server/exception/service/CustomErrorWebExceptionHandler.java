package com.lolclone.api_gateway_server.exception.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolclone.api_gateway_server.exception.commonexception.SerializationException;
import com.lolclone.api_gateway_server.exception.dto.ExceptionResponse;

import reactor.core.publisher.Mono;

import static com.lolclone.api_gateway_server.exception.domain.ExceptionType.SERIALIZATION_EXCEPTION;
import static com.lolclone.api_gateway_server.exception.domain.ExceptionType.UNHANDLED_EXCEPTION;

public class CustomErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    private static final ErrorAttributeOptions DEV_ERROR_ATTRIBUTE_OPTIONS = ErrorAttributeOptions.of(
        ErrorAttributeOptions.Include.EXCEPTION,
        ErrorAttributeOptions.Include.STACK_TRACE,
        ErrorAttributeOptions.Include.MESSAGE,
        ErrorAttributeOptions.Include.BINDING_ERRORS
    );

    private static final ErrorAttributeOptions PROD_ERROR_ATTRIBUTE_OPTIONS = ErrorAttributeOptions.of(
        ErrorAttributeOptions.Include.EXCEPTION,
        ErrorAttributeOptions.Include.MESSAGE
    );

    public CustomErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext, ObjectMapper objectMapper) {
        super(errorAttributes, resources, applicationContext);
        this.objectMapper = objectMapper;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::ErrorResponse);
    }

    protected Mono<ServerResponse> ErrorResponse(ServerRequest request) {
        try {
            Map<String, Object> errorAttributes = getErrorAttributes(request, DEV_ERROR_ATTRIBUTE_OPTIONS);
            String message = errorAttributes.get("message").toString();
            Integer status = Optional.ofNullable(errorAttributes.get("status"))
                .map(s -> (Integer) s)
                .orElse(500);
            String code = UNHANDLED_EXCEPTION.getCode();
            
            return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(convertToJson(new ExceptionResponse(code, message)));
        } catch (IOException e) {
            throw new SerializationException(SERIALIZATION_EXCEPTION);
        }
    }

    private String convertToJson(final ExceptionResponse exceptionResponse) throws IOException {
        return objectMapper.writeValueAsString(exceptionResponse);
    }
}
