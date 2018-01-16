package com.github.pig.mc.listener;

import com.github.pig.common.constant.MqQueueConstant;
import com.github.pig.common.util.sms.AliDaYuSendUtils;
import com.github.pig.common.util.template.MobileMsgTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2018年01月15日13:51:53
 * 监听短信发送请求
 */
@Slf4j
@Component
@RabbitListener(queues = MqQueueConstant.MOBILE_CODE_QUEUE)
public class MobileCodeReceiveListener {
    @RabbitHandler
    public void receive(MobileMsgTemplate mobileMsgTemplate) {

        boolean status = AliDaYuSendUtils.sendSmsCode(
                AliDaYuSendUtils.CHANNEL,
                AliDaYuSendUtils.SIGN_NAME_LOGIN,
                mobileMsgTemplate.getMobile(),
                mobileMsgTemplate.getText());
        log.info("消息中心接收到短信发送请求-> 手机号：{} -> 验证码: {} -> 发送状态：{}", mobileMsgTemplate.getMobile(), mobileMsgTemplate.getText(), status);
    }
}
