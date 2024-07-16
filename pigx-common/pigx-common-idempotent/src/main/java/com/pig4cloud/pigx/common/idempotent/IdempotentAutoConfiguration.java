package com.pig4cloud.pigx.common.idempotent;

import com.pig4cloud.pigx.common.idempotent.aspect.IdempotentAspect;
import com.pig4cloud.pigx.common.idempotent.expression.ExpressionResolver;
import com.pig4cloud.pigx.common.idempotent.expression.KeyResolver;
import org.redisson.Redisson;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lengleng
 * @date 2020/9/25
 * <p>
 * 幂等插件初始化
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class IdempotentAutoConfiguration {


    /**
     * KEY解析器
     *
     * @return {@link KeyResolver }
     */
    @Bean
    @ConditionalOnMissingBean(KeyResolver.class)
    public KeyResolver keyResolver() {
        return new ExpressionResolver();
    }

    /**
     * 幂等方面
     *
     * @param redisson    redisson 客户端
     * @param keyResolver KEY解析器
     * @return {@link IdempotentAspect }
     */
    @Bean
    public IdempotentAspect idempotentAspect(Redisson redisson, KeyResolver keyResolver) {
        return new IdempotentAspect(redisson, keyResolver);
    }

}
