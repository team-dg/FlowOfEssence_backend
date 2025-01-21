package com.lolclone.chatinfra.saga.producer;

import com.lolclone.chatdomain.domain.MemberDomainEvent;
import com.lolclone.chatdomain.domain.member.Member;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;

public class ChatDomainEventPublisher extends AbstractAggregateDomainEventPublisher<Member, MemberDomainEvent>{
    public ChatDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        super(domainEventPublisher, Member.class, Member::getId);
    }
}
