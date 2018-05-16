package com.github.pig.gateway.feign;

import com.github.pig.common.entity.SysZuulRoute;
import com.github.pig.gateway.feign.fallback.MenuServiceFallbackImpl;
import com.github.pig.gateway.feign.fallback.ZuulRouteServiceFallbackImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author lengleng
 * @date 2018/5/15
 * 远程获取zuul配置
 */
@FeignClient(name = "pig-upms-service", fallback = ZuulRouteServiceFallbackImpl.class)
public interface ZuulRouteService {
    /**
     * 调用upms查询全部的路由配置
     *
     * @return 路由配置表
     */
    @GetMapping(value = "/route/findAllZuulRoute")
    List<SysZuulRoute> findAllZuulRoute();
}
