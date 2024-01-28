package com.pig4cloud.pig.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.gateway.filter.PasswordDecoderFilter;
import com.pig4cloud.pig.gateway.filter.PigRequestGlobalFilter;
import com.pig4cloud.pig.gateway.filter.ValidateCodeGatewayFilter;
import com.pig4cloud.pig.gateway.handler.GlobalExceptionHandler;
import com.pig4cloud.pig.gateway.handler.ImageCodeHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 网关配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GatewayConfigProperties.class)
public class GatewayConfiguration {

    /**
     * 创建密码解码器过滤器
     *
     * @param modifyRequestBodyGatewayFilterFactory 修改请求体网关过滤器工厂
     * @param configProperties                      配置属性
     * @return 密码解码器过滤器
     */
    @Bean
    public PasswordDecoderFilter passwordDecoderFilter(
            ModifyRequestBodyGatewayFilterFactory modifyRequestBodyGatewayFilterFactory,
            GatewayConfigProperties configProperties) {
        return new PasswordDecoderFilter(modifyRequestBodyGatewayFilterFactory, configProperties);
    }

    /**
     * 创建PigRequest全局过滤器
     *
     * @return PigRequest全局过滤器
     */
    @Bean
    public PigRequestGlobalFilter pigRequestGlobalFilter() {
        return new PigRequestGlobalFilter();
    }

    /**
     * 创建验证码网关过滤器
     *
     * @param configProperties 配置属性
     * @param redisTemplate    Redis模板
     * @return 验证码网关过滤器
     */
    @Bean
    public ValidateCodeGatewayFilter validateCodeGatewayFilter(GatewayConfigProperties configProperties, RedisTemplate redisTemplate) {
        return new ValidateCodeGatewayFilter(configProperties, redisTemplate);
    }

    /**
     * 创建全局异常处理程序
     *
     * @param objectMapper 对象映射器
     * @return 全局异常处理程序
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
        return new GlobalExceptionHandler(objectMapper);
    }

    /**
     * 创建图片验证码处理器
     *
     * @param redisTemplate Redis模板
     * @return 图片验证码处理器
     */
    @Bean
    public ImageCodeHandler imageCodeHandler(RedisTemplate redisTemplate) {
        return new ImageCodeHandler(redisTemplate);
    }

}
