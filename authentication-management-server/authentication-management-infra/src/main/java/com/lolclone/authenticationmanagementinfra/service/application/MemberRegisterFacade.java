package com.lolclone.authenticationmanagementinfra.service.application;

import java.net.URI;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.lolclone.authenticationmanagementdomain.domain.Member;
import com.lolclone.authenticationmanagementinfra.config.MicroServiceProperties;
import com.lolclone.authenticationmanagementinfra.exception.domain.ExceptionType;
import com.lolclone.authenticationmanagementinfra.exception.microexception.ChatServiceException;
import com.lolclone.authenticationmanagementinfra.exception.microexception.MatchingServiceException;
import com.lolclone.authenticationmanagementserviceapi.dto.MemberDTO;
import com.lolclone.authenticationmanagementserviceapi.dto.SignUpRequest;
import com.lolclone.authenticationmanagementserviceapi.dto.TokenRefreshResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(MicroServiceProperties.class)
public class MemberRegisterFacade {
    private final UserAuthService userAuthService;
    private final RestTemplate restTemplate;
    private final MicroServiceProperties microServiceProperties;
    
    /**
     * 회원가입 프로세스를 처리합니다.
     * 1. 인증 서버에 회원가입
     * 2. 채팅 서버에 회원 등록
     * 3. 매칭 서버에 회원 등록
     * 4. 토큰 발급 및 자동 로그인
     *
     * @param signUpRequest 회원가입 요청 정보
     * @return 회원가입 결과와 토큰 정보
     * @throws ChatServiceException 채팅 서버 등록 실패 시
     * @throws MatchingServiceException 매칭 서버 등록 실패 시
     */
    @Transactional
    public TokenRefreshResponse registerMemberAcrossServices(SignUpRequest signUpRequest) 
            throws ChatServiceException, MatchingServiceException {

        UUID memberId = null;
        boolean authRegistered = false; // 인증 서버 등록 성공 여부 플래그
        boolean chatRegistered = false; // 채팅 서버 등록 성공 여부 플래그
        
        try {
            // 1. 인증 서버에 회원가입
            Member member = userAuthService.originalSignUp(signUpRequest);
            memberId = member.getId();
            authRegistered = true;

            // 2. 채팅 서버에 회원 등록
            registerMemberInRemoteService(member, microServiceProperties.chatServiceUrl(), "/api/v1/members", ChatServiceException::new);
            chatRegistered = true;

            // 3. 매칭 서버에 회원 등록
            registerMemberInRemoteService(member, microServiceProperties.matchingServiceUrl(), "/api/v1/members", MatchingServiceException::new);
        } catch (ChatServiceException  e) {
            log.error("채팅 서버 회원 등록 실패: {}" + e.getMessage()); // 인자를 먼저 계산한다. -> + 연산을 통해 하나의 완성된 문자열 객체로 만들어진다. -> 이 과정에서 컴파일러가 최적화해서 내부적으로 StringBuilder를 사용하게 된다. -> 완성된 객체가 log.info로 전달된다. => 여기서 확인 INFO 레벨이 비활성화 되어있다면 이건? 버린다. 즉 불필요한 문자열 결합 연산 + 객체 생성 비용을 이미 치른 셈
            if (authRegistered) {
                rollbackAuthServiceRegistration(memberId);
            }
            throw e;
        } catch (MatchingServiceException e) {
            log.error("매칭 서버 회원 등록 실패, 롤백 진행: {}", e.getMessage());
            if (chatRegistered) {
                rollbackChatServiceRegistration(memberId);
            }
            throw e;
        } catch (Exception e) {
            log.error("회원 등록 중 예외 발생, 롤백 시도: {}", e.getMessage());
            if (chatRegistered) {
                rollbackChatServiceRegistration(memberId);
            }
            if (authRegistered) {
                rollbackAuthServiceRegistration(memberId);
            }
            throw new RuntimeException(String.format("회원 등록 중 예상치 못한 오류 발생: %s", e.getMessage()), e);
        }

        // 4. 토큰 발급 및 자동 로그인
        TokenRefreshResponse tokenResponse = userAuthService.createSession(memberId);
        return tokenResponse;
    }
    
    /**
     * 원격 마이크로서비스에 회원을 등록하는 공통 메서드.
     *
     * @param member 등록할 회원 정보
     * @param baseUrl 대상 서비스의 기본 URL
     * @param path API 경로
     * @param exceptionConstructor 등록 실패 시 생성할 예외의 생성자 (메시지 두 개 받는 형태)
     * @throws E 지정된 타입의 서비스 예외
     */
    private <E extends RuntimeException> void registerMemberInRemoteService(
        Member member,
        String baseUrl,
        String path,
        BiFunction<ExceptionType, String, E> exceptionConstructor
    ) throws E {
        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getNickname());
        
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
            .path(path)
            .build()
            .toUri();
        
        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(uri, memberDTO, Object.class);
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw exceptionConstructor.apply(ExceptionType.MICROSERVICE_SERVICE_EXCEPTION, response.getStatusCode().toString());
            }
        } catch (RestClientException e) {
            throw exceptionConstructor.apply(ExceptionType.SERVICE_COMMUNICATION_ERROR, e.getMessage());
        } catch (Exception e) {
            throw exceptionConstructor.apply(ExceptionType.EXCEPTION, e.getMessage());
        }
    }

    /**
     * 채팅 서버에서 회원 등록을 롤백합니다.
     * 
     * @param memberId 롤백할 회원 ID
     */
    private void rollbackChatServiceRegistration(UUID memberId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(microServiceProperties.chatServiceUrl())
                .path("/api/v1/members/{memberId}")
                .buildAndExpand(memberId)
                .toUri();

            restTemplate.delete(uri);
        } catch (RestClientException e) {
            log.error("채팅 서버 회원 등록 롤백 통신 중 오류: memberId={}, error={}", memberId, e.getMessage());
        } catch (Exception e) {
            log.error("채팅 서버 회원 등록 롤백 중 예외 발생: memberId={}, error={}", memberId, e.getMessage());
        }
    }

    private void rollbackAuthServiceRegistration(UUID memberId) {
        try {
            userAuthService.cancelSignUp(memberId);
        } catch (Exception e) {
            log.error("인증 서버 회원 등록 롤백 실패: memberId={}, error={}", memberId, e.getMessage());
        }
    }
}
