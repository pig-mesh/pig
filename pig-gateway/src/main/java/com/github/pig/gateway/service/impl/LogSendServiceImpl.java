package com.github.pig.gateway.service.impl;

import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.entity.SysLog;
import com.github.pig.common.util.UserUtils;
import com.github.pig.common.vo.LogVo;
import com.github.pig.gateway.service.LogSendService;
import com.netflix.zuul.context.RequestContext;
import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.util.URLUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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
    public void send(RequestContext requestContext) {
        HttpServletRequest request = requestContext.getRequest();
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        SysLog log = new SysLog();
        log.setRemoteAddr(HttpUtil.getClientIP(request));
        log.setRequestUri(URLUtil.getPath(requestUri));
        log.setMethod(method);
        log.setUserAgent(request.getHeader("user-agent"));
        log.setParams(HttpUtil.toParams(request.getParameterMap()));
        log.setCreateBy(UserUtils.getUserName(request));
        Long startTime = (Long) requestContext.get("startTime");
        log.setTime(System.currentTimeMillis() - startTime);
        LogVo logVo = new LogVo();
        logVo.setSysLog(log);
        //解析用户名的事情异步到rabbit消费中处理
        if (StringUtils.isNotEmpty(request.getHeader(CommonConstant.REQ_HEADER))) {
            logVo.setToken(request.getHeader(CommonConstant.REQ_HEADER));
        }
        try {
            rabbitTemplate.convertAndSend(CommonConstant.LOG_QUEUE, logVo);
        }catch (Exception e) {
            logger.error("MQ发送日志异常，异常信息："+e.getMessage());
        }
    }
}
