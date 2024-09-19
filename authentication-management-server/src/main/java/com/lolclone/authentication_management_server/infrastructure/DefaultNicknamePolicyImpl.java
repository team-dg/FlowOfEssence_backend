package com.lolclone.authentication_management_server.infrastructure;

import java.util.concurrent.ThreadLocalRandom;

import com.lolclone.authentication_management_server.domain.DefaultNicknamePolicy;
import com.lolclone.common_module.exception.domain.ExceptionType;

import com.lolclone.common_module.commonexception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultNicknamePolicyImpl implements DefaultNicknamePolicy {

    @Override
    public String generate() {
        int maxNumber = 10000;
        if(maxNumber <= 0) throw new BadRequestException(ExceptionType.INVALID_NICKNAME_MAX_NUMBER);
        int randomNumber = ThreadLocalRandom.current().nextInt(1, maxNumber + 1);
        return "User" + String.format("%04d", randomNumber);
    }
}
