package com.lolclone.authenticationmanagementinfra.nickname;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.lolclone.authenticationmanagementdomain.domain.DefaultNicknamePolicy;
import com.lolclone.authenticationmanagementdomain.exception.NicknameGenerationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultNicknamePolicyImpl implements DefaultNicknamePolicy {

    @Override
    public String generate() {
        int maxNumber = 10000;
        if(maxNumber <= 0) throw new NicknameGenerationException();
        int randomNumber = ThreadLocalRandom.current().nextInt(1, maxNumber + 1);
        return "User" + String.format("%04d", randomNumber);
    }
}
