package com.github.pig.gateway.service.impl;

import com.github.pig.common.constant.CommonConstant;
import com.github.pig.gateway.service.LogSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2017/11/16
 * 消息发往消息队列工具类
 */
@Component
public class LogSendServiceImpl implements LogSendService {
    private Logger logger = LoggerFactory.getLogger(LogSendServiceImpl.class);
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public void send() {
        try {
            rabbitTemplate.convertAndSend(CommonConstant.LOG_QUEUE, "你好");
        } catch (Exception connectException) {
            logger.error("rabbitMQ链接异常", connectException);
        }
    }
}
