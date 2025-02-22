package com.lolclone.userinfra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.eventuate.common.jdbc.EventuateJdbcStatementExecutor;
import io.eventuate.common.jdbc.EventuateSchema;
import io.eventuate.common.jdbc.EventuateTransactionTemplate;
import io.eventuate.tram.consumer.common.DuplicateMessageDetector;
import io.eventuate.tram.consumer.jdbc.SqlTableBasedDuplicateMessageDetector;

@Configuration
public class SagaConfiguration {
    @Autowired
    private EventuateSchema eventuateSchema;

    @Autowired
    private EventuateJdbcStatementExecutor eventuateJdbcStatementExecutor;

    @Autowired
    private EventuateTransactionTemplate eventuateTransactionTemplate;

    @Bean
    public DuplicateMessageDetector duplicateMessageDetector() {
        String currentTimeInMillisecondsSql = "CURRENT_TIMESTAMP(3)";
        return new SqlTableBasedDuplicateMessageDetector(eventuateSchema, currentTimeInMillisecondsSql, eventuateJdbcStatementExecutor, eventuateTransactionTemplate);
    }
}
