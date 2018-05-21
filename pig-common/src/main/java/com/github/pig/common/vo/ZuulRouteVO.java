package com.github.pig.common.vo;

import lombok.Data;

/**
 * @author lengleng
 * @date 2018/5/15
 * 自定义路由实体
 */
@Data
public class ZuulRouteVO {
    /**
     * router ID
     */
    private String id;

    /**
     * The path (pattern) for the route, e.g. /foo/**.
     */
    private String path;

    /**
     * The service ID (if any) to map to this route. You can specify a physical URL or
     * a service, but not both.
     */
    private String serviceId;

    /**
     * A full physical URL to map to the route. An alternative is to use a service ID
     * and service discovery to find the physical address.
     */
    private String url;

    /**
     * Flag to determine whether the prefix for this route (the path, minus pattern
     * patcher) should be stripped before forwarding.
     */
    private Boolean stripPrefix;

    /**
     * Flag to indicate that this route should be retryable (if supported). Generally
     * retry requires a service ID and ribbon.
     */
    private Boolean retryable;

    /**
     * 是否开启
     */
    private Boolean enabled;

    /**
     * 微服务敏感的header
     */
    private String sensitiveHeadersList;
}
