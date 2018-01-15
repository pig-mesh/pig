package com.github.pig.admin.common.listener;

import com.github.pig.admin.service.SysLogService;
import com.github.pig.common.constant.MqQueueConstant;
import com.github.pig.common.entity.SysLog;
import com.github.pig.common.util.UserUtils;
import com.github.pig.common.vo.LogVo;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2017/11/17
 */
@Component
@RabbitListener(queues = MqQueueConstant.LOG_QUEUE)
public class LogReceiveListener {
    private static final String KEY_USER = "user";

    @Autowired
    private SysLogService sysLogService;

    @RabbitHandler
    public void receive(LogVo logVo) {
        System.out.println(logVo.getSysLog());
        SysLog sysLog = logVo.getSysLog();
        String username = UserUtils.getUserName(logVo.getToken());
        MDC.put(KEY_USER, username);
        sysLog.setCreateBy(username);
        sysLogService.insert(sysLog);
        MDC.remove(KEY_USER);
    }
}
