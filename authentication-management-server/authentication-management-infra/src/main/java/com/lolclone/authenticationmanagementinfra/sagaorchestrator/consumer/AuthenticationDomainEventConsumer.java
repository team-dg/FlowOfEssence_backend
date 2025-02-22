package com.lolclone.authenticationmanagementinfra.sagaorchestrator.consumer;

import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authenticationmanagementinfra.service.application.UserAuthService;
import com.lolclone.userserviceapi.event.MemberCreated;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationDomainEventConsumer {
    private static final String MEMBER_AGGREGATE_TYPE = "com.lolclone.userdomain.entity.Member";
    private final UserAuthService userAuthService;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType(MEMBER_AGGREGATE_TYPE)
                .onEvent(MemberCreated.class, this::createMember)
                .build();
    }

    @Transactional
    private void createMember(DomainEventEnvelope<MemberCreated> memberCreated) {
        
    }
}
