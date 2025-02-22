package com.lolclone.chatinfra.config.saga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.eventuate.common.jdbc.EventuateJdbcStatementExecutor;
import io.eventuate.common.jdbc.EventuateSchema;
import io.eventuate.common.jdbc.EventuateTransactionTemplate;
import io.eventuate.tram.consumer.common.DuplicateMessageDetector;
import io.eventuate.tram.consumer.jdbc.SqlTableBasedDuplicateMessageDetector;

@Configuration
public class SagaConfiguration {
    private static final String DUPLICATE_MESSAGE_DETECTOR_BEAN_NAME = "chatDuplicateMessageDetector";
    @Autowired
    private EventuateSchema eventuateSchema;

    @Autowired
    private EventuateJdbcStatementExecutor eventuateJdbcStatementExecutor;

    @Autowired
    private EventuateTransactionTemplate eventuateTransactionTemplate;

    @Bean(DUPLICATE_MESSAGE_DETECTOR_BEAN_NAME)
    @Primary
    public DuplicateMessageDetector duplicateMessageDetector() {
        String currentTimeInMillisecondsSql = "EXTRACT(EPOCH FROM CURRENT_TIMESTAMP) * 1000";
        return new SqlTableBasedDuplicateMessageDetector(eventuateSchema, currentTimeInMillisecondsSql, eventuateJdbcStatementExecutor, eventuateTransactionTemplate);
    }
}
