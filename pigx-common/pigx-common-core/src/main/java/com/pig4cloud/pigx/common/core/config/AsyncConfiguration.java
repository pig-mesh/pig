package com.pig4cloud.pigx.common.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 明确异步线程池配置，避免和 @EnableScheduling 造成的线程池冲突
 *
 * @author lengleng
 * @date 2024/4/20
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class AsyncConfiguration implements AsyncConfigurer {

    private final ThreadPoolTaskExecutor applicationTaskExecutor;

    @Override
    public Executor getAsyncExecutor() {
        return applicationTaskExecutor;
    }
}
