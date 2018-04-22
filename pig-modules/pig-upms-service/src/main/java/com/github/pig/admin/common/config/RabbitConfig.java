package com.github.pig.admin.common.config;

import com.github.pig.common.constant.MqQueueConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lengleng
 * @date 2017/11/16
 * rabbit初始化配置
 */
@Configuration
public class RabbitConfig {
    /**
     * 初始化 log队列
     *
     * @return
     */
    @Bean
    public Queue initLogQueue() {
        return new Queue(MqQueueConstant.LOG_QUEUE);
    }

    /**
     * 初始化 手机验证码通道
     *
     * @return
     */
    @Bean
    public Queue initMobileCodeQueue() {
        return new Queue(MqQueueConstant.MOBILE_CODE_QUEUE);
    }

    /**
     * 初始化服务状态改变队列
     *
     * @return
     */
    @Bean
    public Queue initMobileServiceStatusChangeQueue() {
        return new Queue(MqQueueConstant.MOBILE_SERVICE_STATUS_CHANGE);
    }

    @Bean
    public Queue initDingTalkServiceStatusChangeQueue() {
        return new Queue(MqQueueConstant.DINGTALK_SERVICE_STATUS_CHANGE);
    }

    @Bean
    public Queue initZipkinQueue() {
        return new Queue(MqQueueConstant.ZIPKIN_NAME_QUEUE);
    }
}
