package com.github.pig.gateway;

import com.github.pig.gateway.component.config.DynamicRouteLocator;
import com.github.pig.gateway.feign.ZuulRouteService;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author lengleng
 * @date 2018/5/15
 * 动态路由配置类
 */
@Configuration
public class DynamicRouteConfiguration {
    private Registration registration;
    private DiscoveryClient discovery;
    private ZuulProperties zuulProperties;
    private ServerProperties server;
    private ZuulRouteService zuulRouteService;

    public DynamicRouteConfiguration(ZuulRouteService zuulRouteService, Registration registration, DiscoveryClient discovery, ZuulProperties zuulProperties, ServerProperties server) {
        this.registration = registration;
        this.discovery = discovery;
        this.zuulProperties = zuulProperties;
        this.server = server;
        this.zuulRouteService = zuulRouteService;
    }

    @Bean
    public DynamicRouteLocator dynamicRouteLocator() {
        return new DynamicRouteLocator(server.getServletPrefix()
                , discovery
                , zuulProperties
                , registration
                , zuulRouteService);
    }
}
