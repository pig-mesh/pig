package com.github.pig.gateway.componet.handler;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.RateLimiterErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lengleng
 * @date 2018/2/26
 * 限流降级处理
 */
@Slf4j
@Configuration
public class ZuulRateLimiterErrorHandler {

    @Bean
    public RateLimiterErrorHandler rateLimitErrorHandler() {
        return new DefaultRateLimiterErrorHandler() {
            @Override
            public void handleSaveError(String key, Exception e) {
                log.error("保存key:[{}]异常", key, e);
            }

            @Override
            public void handleFetchError(String key, Exception e) {
                log.error("路由失败:[{}]异常", key);
            }

            @Override
            public void handleError(String msg, Exception e) {
                log.error("限流异常:[{}]", msg, e);
            }
        };
    }
}
