package com.github.pig.gateway.filter;

import com.github.pig.gateway.service.LogSendService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;

/**
 * @author lengleng
 * @date 2017/11/16
 * 网关日志拦截器
 */
@Component
public class LoggerFilter extends ZuulFilter {
    private static final String KEY_USER = "user";

    @Autowired
    private LogSendService logSendService;

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        MDC.put(KEY_USER, "冷冷");
        logSendService.send(RequestContext.getCurrentContext());
        MDC.remove(KEY_USER);
        return null;
    }
}
