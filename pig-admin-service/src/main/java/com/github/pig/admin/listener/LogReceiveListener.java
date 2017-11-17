package com.github.pig.admin.listener;

import com.github.pig.common.constant.CommonConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2017/11/17
 */
@Component
@RabbitListener(queues = CommonConstant.LOG_QUEUE)
public class LogReceiveListener {
    @RabbitHandler
    public void receive(String text) {
        System.out.println("------------------------->" + text);
    }
}
