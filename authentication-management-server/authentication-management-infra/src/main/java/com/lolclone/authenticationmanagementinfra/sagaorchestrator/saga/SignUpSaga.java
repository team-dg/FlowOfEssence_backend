package com.lolclone.authenticationmanagementinfra.sagaorchestrator.saga;

import com.lolclone.authenticationmanagementinfra.sagaorchestrator.sagaparticipants.AuthenticationServiceProxy;
import com.lolclone.authenticationmanagementinfra.sagaorchestrator.sagaparticipants.UserServiceProxy;

import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SignUpSaga implements SimpleSaga<SignUpSagaState>{
    private SagaDefinition<SignUpSagaState> sagaDefinition;

    public SignUpSaga(UserServiceProxy userServiceProxy, AuthenticationServiceProxy authenticationServiceProxy) {
        this.sagaDefinition = 
                step()
                    .invokeParticipant(userServiceProxy.createUser, SignUpSagaState::makeCreateUserCommand)
                    .withCompensation(userServiceProxy.undoCreateUser, SignUpSagaState::makeUndoCreateUserCommand)
                .step()
                    .invokeParticipant(authenticationServiceProxy.createUser, SignUpSagaState::makeCreateSignUpUserCommand)
                    .withCompensation(authenticationServiceProxy.undoCreateUser, SignUpSagaState::makeUndoCreateSignUpUserCommand)
                .build();
    }

    @Override
    public SagaDefinition<SignUpSagaState> getSagaDefinition() {
        if (sagaDefinition == null) {
            throw new IllegalStateException("sagaDefinition must not be null");
        }
        return sagaDefinition;
    }
}
