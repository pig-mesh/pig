package com.github.pig.gateway.service.impl;

import com.github.pig.gateway.service.LogSendService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2017/11/16
 * 消息发往消息队列工具类
 */
@Component
public class LogSendServiceImpl implements LogSendService {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void send() {
        this.amqpTemplate.convertAndSend("log", "hello");
    }
}
