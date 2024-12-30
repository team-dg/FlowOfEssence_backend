package com.lolclone.authenticationmanagementinfra.sagaorchestrator.producer;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;

import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.authenticationmanagementdomain.domain.MemberDomainEvent;

public class AuthenticationDomainEventPublisher extends AbstractAggregateDomainEventPublisher<Member, MemberDomainEvent> {
    public AuthenticationDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        super(domainEventPublisher, Member.class, Member::getId);
    }
}
