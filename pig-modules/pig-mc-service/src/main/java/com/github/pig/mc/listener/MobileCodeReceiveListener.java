package com.github.pig.mc.listener;

import com.github.pig.common.constant.MqQueueConstant;
import com.github.pig.common.util.template.MobileMsgTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2018年01月15日13:51:53
 * 监听短信发送请求
 */
@Component
@RabbitListener(queues = MqQueueConstant.MOBILE_CODE_QUEUE)
public class MobileCodeReceiveListener {
    private static final Logger logger = LoggerFactory.getLogger(MobileCodeReceiveListener.class);

    @RabbitHandler
    public void receive(MobileMsgTemplate mobileMsgTemplate) {
        logger.info("消息中心接收到短信发送请求-> 手机号：{} -> 验证码: {} ", mobileMsgTemplate.getMobile(), mobileMsgTemplate.getText());
    }
}
