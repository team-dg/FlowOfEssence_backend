package com.lolclone.authenticationmanagementinfra.config.saga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.lolclone.authenticationmanagementinfra.sagaorchestrator.saga.SignUpSaga;
import com.lolclone.authenticationmanagementinfra.sagaorchestrator.saga.SignUpSagaState;
import com.lolclone.authenticationmanagementinfra.sagaorchestrator.sagaparticipants.AuthenticationServiceProxy;
import com.lolclone.authenticationmanagementinfra.sagaorchestrator.sagaparticipants.ChatServiceProxy;
import com.lolclone.authenticationmanagementinfra.sagaorchestrator.sagaparticipants.UserServiceProxy;

import io.eventuate.common.jdbc.EventuateJdbcStatementExecutor;
import io.eventuate.common.jdbc.EventuateSchema;
import io.eventuate.common.jdbc.EventuateTransactionTemplate;
import io.eventuate.tram.commands.producer.CommandProducer;
import io.eventuate.tram.consumer.common.DuplicateMessageDetector;
import io.eventuate.tram.consumer.jdbc.SqlTableBasedDuplicateMessageDetector;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.sagas.common.SagaLockManager;
import io.eventuate.tram.sagas.orchestration.SagaCommandProducer;
import io.eventuate.tram.sagas.orchestration.SagaInstanceRepository;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.eventuate.tram.sagas.orchestration.SagaManagerImpl;;

@Configuration
@EnableAutoConfiguration
public class SagaConfiguration {
    @Autowired
    private EventuateSchema eventuateSchema;

    @Autowired
    private EventuateJdbcStatementExecutor eventuateJdbcStatementExecutor;

    @Autowired
    private EventuateTransactionTemplate eventuateTransactionTemplate;
    
    @Bean
    public SagaManager<SignUpSagaState> signUpSagaManager(
        SignUpSaga saga,
        SagaInstanceRepository sagaInstanceRepository,
        CommandProducer commandProducer,
        MessageConsumer messageConsumer,
        SagaLockManager sagaLockManager,
        SagaCommandProducer sagaCommandProducer
        ) {
        SagaManagerImpl<SignUpSagaState> sagaManager = new SagaManagerImpl<>(
            saga,
            sagaInstanceRepository,
            commandProducer,
            messageConsumer,
            sagaLockManager,
            sagaCommandProducer
        );
        
        sagaManager.subscribeToReplyChannel();

        return sagaManager;
    }

    @Bean
    public SignUpSaga signUpSaga(UserServiceProxy userServiceProxy, AuthenticationServiceProxy authenticationServiceProxy, ChatServiceProxy chatServiceProxy) {
        return new SignUpSaga(userServiceProxy, authenticationServiceProxy, chatServiceProxy);
    }
    
    @Bean
    public UserServiceProxy userServiceProxy() {
        return new UserServiceProxy();
    }

    @Bean
    public ChatServiceProxy chatServiceProxy() {
        return new ChatServiceProxy();
    }

    @Bean
    public AuthenticationServiceProxy authenticationServiceProxy() {
        return new AuthenticationServiceProxy();
    }

    @Bean
    public DuplicateMessageDetector duplicateMessageDetector() {
        String currentTimeInMillisecondsSql = "CURRENT_TIMESTAMP(3)";
        return new SqlTableBasedDuplicateMessageDetector(eventuateSchema, currentTimeInMillisecondsSql, eventuateJdbcStatementExecutor, eventuateTransactionTemplate);
    }
}