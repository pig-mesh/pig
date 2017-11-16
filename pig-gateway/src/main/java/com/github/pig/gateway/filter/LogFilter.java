package com.github.pig.gateway.filter;

import com.github.pig.gateway.service.LogSendService;
import com.netflix.zuul.ZuulFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2017/11/16
 * 网关日志拦截器
 */
@Component
public class LogFilter extends ZuulFilter {
    @Autowired
    private LogSendService logSendService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        logSendService.send();
        return null;
    }
}
