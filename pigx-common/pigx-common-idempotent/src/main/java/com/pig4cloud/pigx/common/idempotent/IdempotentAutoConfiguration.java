package com.pig4cloud.pigx.common.idempotent;

import com.pig4cloud.pigx.common.idempotent.aspect.IdempotentAspect;
import com.pig4cloud.pigx.common.idempotent.expression.ExpressionResolver;
import com.pig4cloud.pigx.common.idempotent.expression.KeyResolver;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author lengleng
 * @date 2020/9/25
 * <p>
 * 幂等插件初始化
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(DataRedisAutoConfiguration.class)
@EnableConfigurationProperties(DataRedisProperties.class)
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
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(DataRedisProperties redisProperties) {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setDatabase(redisProperties.getDatabase());

        if (StringUtils.hasText(redisProperties.getUsername())) {
            singleServerConfig.setUsername(redisProperties.getUsername());
        }
        if (StringUtils.hasText(redisProperties.getPassword())) {
            singleServerConfig.setPassword(redisProperties.getPassword());
        }
        if (redisProperties.getConnectTimeout() != null) {
            singleServerConfig.setConnectTimeout((int) redisProperties.getConnectTimeout().toMillis());
        }
        if (redisProperties.getTimeout() != null) {
            singleServerConfig.setTimeout((int) redisProperties.getTimeout().toMillis());
        }

        return Redisson.create(config);
    }

    @Bean
    public IdempotentAspect idempotentAspect(RedissonClient redisson, KeyResolver keyResolver) {
        return new IdempotentAspect(redisson, keyResolver);
    }

}
