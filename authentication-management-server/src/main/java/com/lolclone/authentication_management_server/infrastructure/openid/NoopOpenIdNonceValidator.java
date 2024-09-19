package com.lolclone.authentication_management_server.infrastructure.openid;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import com.lolclone.authentication_management_server.domain.OpenIdNonceValidator;
import com.lolclone.common_module.commonexception.ValidException;
import com.lolclone.common_module.exception.domain.ExceptionType;
import com.lolclone.common_module.util.Validator;

/**
 * 1. nonce의 유효성을 검사함
 * 2. 토큰의 만료 여부를 확인
 * 3. nonce의 재사용을 방지함
 * 4. 만료된 nonce를 주기적으로 제거함
 */

@Component
public class NoopOpenIdNonceValidator implements OpenIdNonceValidator {
    //private final ConcurrentMap<String, Date> usedNonces = new ConcurrentHashMap<>();

    @Override
    public void validate(String nonce, Date expiredAt) {
        // try {
        //     Validator.notBlank(nonce, "nonce");
        //     Validator.notNull(expiredAt, "expiredAt");
            
        //     if (expiredAt.before(new Date())) {
        //         throw new ValidException("토큰이 만료되었습니다.");
        //     }

        //     if (usedNonces.putIfAbsent(nonce, expiredAt) != null) {
        //         throw new ValidException("이미 사용된 nonce 값입니다.");
        //     }

        //     usedNonces.entrySet().removeIf(entry -> entry.getValue().before(new Date()));
        // } catch (ValidException e) {
        //     throw new ValidException(ExceptionType.VALIDATION_FAIL.getMessage() + ": " + e.getMessage());
        // }
    }
}
