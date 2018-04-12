package com.github.pig.gateway.service.impl;

import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.constant.MqQueueConstant;
import com.github.pig.common.entity.SysLog;
import com.github.pig.common.vo.LogVO;
import com.github.pig.gateway.service.LogSendService;
import com.netflix.zuul.context.RequestContext;
import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.xiaoleilu.hutool.util.URLUtil;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lengleng
 * @date 2017/11/16
 * 消息发往消息队列工具类
 */
@Component
public class LogSendServiceImpl implements LogSendService {
    private static final String SERVICE_ID = "serviceId";
    private Logger logger = LoggerFactory.getLogger(LogSendServiceImpl.class);
    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * 1. 获取 requestContext 中的请求信息
     * 2. 如果返回状态不是OK，则获取返回信息中的错误信息
     * 3. 发送到MQ
     *
     * @param requestContext 上下文对象
     */
    @Override
    public void send(RequestContext requestContext) {
        HttpServletRequest request = requestContext.getRequest();
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        SysLog log = new SysLog();
        log.setType(CommonConstant.STATUS_NORMAL);
        log.setRemoteAddr(HttpUtil.getClientIP(request));
        log.setRequestUri(URLUtil.getPath(requestUri));
        log.setMethod(method);
        log.setUserAgent(request.getHeader("user-agent"));
        log.setParams(HttpUtil.toParams(request.getParameterMap()));
        Long startTime = (Long) requestContext.get("startTime");
        log.setTime(System.currentTimeMillis() - startTime);
        if (requestContext.get(SERVICE_ID) != null) {
            log.setServiceId(requestContext.get(SERVICE_ID).toString());
        }

        //正常发送服务异常解析
        if (requestContext.getResponseStatusCode() != HttpStatus.SC_OK
                && requestContext.getResponseDataStream() != null) {
            InputStream inputStream = requestContext.getResponseDataStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream stream1 = null;
            InputStream stream2;
            byte[] buffer = IoUtil.readBytes(inputStream);
            try {
                baos.write(buffer);
                baos.flush();
                stream1 = new ByteArrayInputStream(baos.toByteArray());
                stream2 = new ByteArrayInputStream(baos.toByteArray());
                String resp = IoUtil.read(stream1, CommonConstant.UTF8);
                log.setType(CommonConstant.STATUS_LOCK);
                log.setException(resp);
                requestContext.setResponseDataStream(stream2);
            } catch (IOException e) {
                logger.error("响应流解析异常：", e);
                throw new RuntimeException(e);
            } finally {
                IoUtil.close(stream1);
                IoUtil.close(baos);
                IoUtil.close(inputStream);
            }

        }

        //网关内部异常
        Throwable throwable = requestContext.getThrowable();
        if (throwable != null) {
            logger.error("网关异常", throwable);
            log.setException(throwable.getMessage());
        }

        //保存发往MQ（只保存授权）
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && StrUtil.isNotBlank(authentication.getName())) {
            LogVO logVo = new LogVO();
            log.setCreateBy(authentication.getName());
            logVo.setSysLog(log);
            logVo.setUsername(authentication.getName());
            rabbitTemplate.convertAndSend(MqQueueConstant.LOG_QUEUE, logVo);
        }
    }
}
