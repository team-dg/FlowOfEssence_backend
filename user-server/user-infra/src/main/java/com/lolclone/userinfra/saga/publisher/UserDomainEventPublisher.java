package com.lolclone.userinfra.saga.publisher;

import com.lolclone.userdomain.entity.Member;
import com.lolclone.userdomain.entity.UserDomainEvent;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;

public class UserDomainEventPublisher extends AbstractAggregateDomainEventPublisher<Member, UserDomainEvent>{
    public UserDomainEventPublisher(DomainEventPublisher eventPublisher) {
        super(eventPublisher, Member.class, Member::getId);
    }
}
