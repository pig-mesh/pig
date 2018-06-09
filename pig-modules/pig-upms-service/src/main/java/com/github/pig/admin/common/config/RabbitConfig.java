/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

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

    /**
     * 初始化钉钉状态改变队列
     *
     * @return
     */
    @Bean
    public Queue initDingTalkServiceStatusChangeQueue() {
        return new Queue(MqQueueConstant.DINGTALK_SERVICE_STATUS_CHANGE);
    }

    /**
     * 初始化zipkin队列
     *
     * @return
     */
    @Bean
    public Queue initZipkinQueue() {
        return new Queue(MqQueueConstant.ZIPKIN_NAME_QUEUE);
    }

    /**
     * 初始化路由配置状态队列
     *
     * @return
     */
    @Bean
    public Queue initRouteConfigChangeQueue() {
        return new Queue(MqQueueConstant.ROUTE_CONFIG_CHANGE);
    }
}
