package com.lolclone.chatinfra.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);     // 기본적으로 실행 대기 중인 스레드 개수
        executor.setMaxPoolSize(10);      // 동시 동작하는 최대 스레드 개수
        executor.setQueueCapacity(25);    // MaxPoolSize 초과 요청에서 Thread 생성 요청 시, 해당 요청을 Queue에 저장하는데 이 때 최대 수용 가능한 Queue의 크기
        executor.setThreadNamePrefix("MessageAsync-"); // 스레드 이름 접두사
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 큐가 꽉 찼을 때 처리 정책
        executor.initialize();
        return executor;
    }
}
/**
 * 스레드 큐에 메시지가 가득 차게 될 경우 이를 처리하는 정책을 작성해야 됨
 */
