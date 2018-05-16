package com.github.pig.gateway.component.config;

import com.github.pig.common.constant.ServiceNameConstant;
import com.github.pig.common.entity.SysZuulRoute;
import com.github.pig.gateway.feign.ZuulRouteService;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lengleng
 * @date 2018/5/15
 * 动态路由实现
 */
@Slf4j
public class DynamicRouteLocator extends DiscoveryClientRouteLocator {
    private ZuulProperties properties;
    private RestTemplate restTemplate;
    private ZuulRouteService zuulRouteService;

    public DynamicRouteLocator(String servletPath, DiscoveryClient discovery, ZuulProperties properties, ServiceInstance localServiceInstance, ZuulRouteService zuulRouteService) {
        super(servletPath, discovery, properties, localServiceInstance);
        this.properties = properties;
        this.zuulRouteService = zuulRouteService;
    }

    /**
     * 重写路由配置
     * <p>
     * 1. properties 配置。
     * 2. eureka 默认配置。
     * 3. DB数据库配置。
     *
     * @return 路由表
     */
    @Override
    protected LinkedHashMap<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>();
        //读取properties配置、eureka默认配置
        routesMap.putAll(super.locateRoutes());
        log.info("初始默认的路由配置完成");
        routesMap.putAll(locateRoutesFromDb());
        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StrUtil.isNotBlank(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return routesMap;
    }

    /**
     * 拉去路由配置先采用RestTemplate 加载eureka中信息来是实现
     *
     * @return
     */
    private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromDb() {
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();

        String url = String.format("http://localhost:4000/route/findAllZuulRoute", ServiceNameConstant.UMPS_SERVICE);
        SysZuulRoute[] response = restTemplate.getForObject(url, SysZuulRoute[].class);
        if (ArrayUtil.isEmpty(response)) {
            return routes;
        }

        for (SysZuulRoute result : response) {
            if (StrUtil.isBlank(result.getPath()) || StrUtil.isBlank(result.getUrl())) {
                continue;
            }

            ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
            try {
                zuulRoute.setId(String.valueOf(result.getId()));
                zuulRoute.setPath(result.getPath());
                zuulRoute.setServiceId(result.getServiceId());
                zuulRoute.setRetryable(StrUtil.equals(result.getRetryable(), "0") ? Boolean.FALSE : Boolean.TRUE);
                zuulRoute.setStripPrefix(StrUtil.equals(result.getStripPrefix(), "0") ? Boolean.FALSE : Boolean.TRUE);
                zuulRoute.setUrl(result.getUrl());
                List<String> sensitiveHeadersList = StrUtil.splitTrim(result.getSensitiveheadersList(), ",");
                if (CollUtil.isNotEmpty(sensitiveHeadersList)) {
                    Set<String> sensitiveHeaderSet = CollUtil.newHashSet();
                    sensitiveHeadersList.forEach(sensitiveHeader -> sensitiveHeaderSet.add(sensitiveHeader));
                    zuulRoute.setSensitiveHeaders(sensitiveHeaderSet);
                    zuulRoute.setCustomSensitiveHeaders(true);
                }
            } catch (Exception e) {
                log.error("从数据库加载路由配置异常", e);
            }
            log.info("添加数据库自定义的路由配置,path：{}，serviceId:{}", zuulRoute.getPath(), zuulRoute.getServiceId());
            routes.put(zuulRoute.getPath(), zuulRoute);
        }
        return routes;
    }
}
