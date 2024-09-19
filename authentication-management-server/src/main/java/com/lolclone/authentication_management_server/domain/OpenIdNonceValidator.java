package com.lolclone.authentication_management_server.domain;

import java.util.Date;

/**
 *  nonce를 기록하고 기록된 nonce의 TTL은 expiredAt 이후로 삭제되게 한다. nonce 값이 이미 기록된 값이 주어지면 사용자의 토큰이 도난
 *  당한 것으로 판단하여 예외를 던져야 한다.
 */

public interface OpenIdNonceValidator {
    void validate(String nonce, Date expiredAt);
}
