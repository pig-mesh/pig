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

package com.github.pig.common.constant;

/**
 * @author lengleng
 * @date 2018/1/15
 * MQ 消息队列
 */
public interface MqQueueConstant {
    /**
     * log rabbit队列名称
     */
    String LOG_QUEUE = "log";

    /**
     * 发送短信验证码队列
     */
    String MOBILE_CODE_QUEUE = "mobile_code_queue";

    /**
     * 短信服务状态队列
     */
    String MOBILE_SERVICE_STATUS_CHANGE = "mobile_service_status_change";

    /**
     * 钉钉服务状态队列
     */
    String DINGTALK_SERVICE_STATUS_CHANGE = "dingtalk_service_status_change";

    /**
     * zipkin 队列
     */
    String ZIPKIN_NAME_QUEUE = "zipkin";

    /**
     * 路由配置状态队列
     */
    String ROUTE_CONFIG_CHANGE = "route_config_change";
}
