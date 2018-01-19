package com.github.pig.mc.listener;

import com.github.pig.common.constant.MqQueueConstant;
import com.github.pig.common.util.template.MobileMsgTemplate;
import com.github.pig.mc.handler.SmsMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lengleng
 * @date 2018年01月15日13:51:53
 * 监听短信发送请求
 */
@Slf4j
@Component
@RabbitListener(queues = MqQueueConstant.MOBILE_CODE_QUEUE)
public class MobileCodeReceiveListener {
    @Autowired
    private Map<String, SmsMessageHandler> messageHandlerMap;

    @RabbitHandler
    public void receive(MobileMsgTemplate mobileMsgTemplate) {
        long startTime = System.currentTimeMillis();
        log.info("消息中心接收到短信发送请求-> 手机号：{} -> 验证码: {} ", mobileMsgTemplate.getMobile(), mobileMsgTemplate.getText());
        String type = mobileMsgTemplate.getType();
        SmsMessageHandler messageHandler = messageHandlerMap.get(type);
        messageHandler.execute(mobileMsgTemplate);
        long useTime = System.currentTimeMillis() - startTime;
        log.info("调用 {} 短信网关处理完毕，耗时 {}毫秒", type, useTime);
    }
}
