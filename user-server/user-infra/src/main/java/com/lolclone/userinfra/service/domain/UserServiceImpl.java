package com.lolclone.userinfra.service.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.userdomain.entity.Member;
import com.lolclone.userdomain.entity.UserDomainEvent;
import com.lolclone.userdomain.repository.UserRepository;
import com.lolclone.userinfra.saga.publisher.UserDomainEventPublisher;
import com.lolclone.userserviceapi.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDomainEventPublisher domainEventPublisher;

    @Override
    @Transactional
    public void createUser(UUID userId, String nickname) {
        Member user = Member.of(userId, nickname);
        userRepository.findById(userId).orElseGet(() -> userRepository.save(user));
        List<UserDomainEvent> events = user.createUser();
        domainEventPublisher.publish(user, events);
    }

    @Override
    @Transactional
    public void undoCreateUser(UUID userId) {
        userRepository.findById(userId)
            .ifPresent(user -> {
                userRepository.delete(user);
                List<UserDomainEvent> events = user.undoCreateUser();
                domainEventPublisher.publish(user, events);
            });
    }
}
