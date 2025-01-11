package com.lolclone.authenticationmanagementinfra.sagaorchestrator.consumer;

import org.springframework.transaction.annotation.Transactional;

import com.lolclone.authenticationmanagementinfra.service.application.UserAuthService;
import com.lolclone.commonmodule.channel.ChannelNames;
import com.lolclone.userserviceapi.event.MemberCreated;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationDomainEventConsumer {
    private final UserAuthService userAuthService;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType(ChannelNames.USER_SERVICE)
                .onEvent(MemberCreated.class, this::createMember)
                .build();
    }

    @Transactional
    private void createMember(DomainEventEnvelope<MemberCreated> memberCreated) {
        
    }
}
