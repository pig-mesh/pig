package com.github.pig.admin.listener;

import com.github.pig.admin.service.SysLogService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.entity.SysLog;
import com.github.pig.common.util.UserUtils;
import com.github.pig.common.vo.LogVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2017/11/17
 */
@Component
@RabbitListener(queues = CommonConstant.LOG_QUEUE)
public class LogReceiveListener {
    @Autowired
    private SysLogService sysLogService;

    @RabbitHandler
    public void receive(LogVo logVo) {
        SysLog sysLog = logVo.getSysLog();
        if (StringUtils.isNotEmpty(logVo.getToken())) {
            String username = UserUtils.getUserName(logVo.getToken());
            sysLog.setCreateBy(username);
        }
        sysLogService.insert(sysLog);
    }
}
