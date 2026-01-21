package com.pig4cloud.pigx.mp.config;

import com.google.common.collect.Maps;
import com.pig4cloud.pigx.admin.api.feign.RemoteTenantService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.util.RetOps;
import com.pig4cloud.pigx.common.data.tenant.TenantBroker;
import com.pig4cloud.pigx.mp.entity.WxAccount;
import com.pig4cloud.pigx.mp.handler.*;
import com.pig4cloud.pigx.mp.service.WxAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.chanjar.weixin.common.api.WxConsts.*;

/**
 * 微信公众号初始化配置类
 * <p>
 * 功能说明：
 * 1. 应用启动时自动加载所有租户的微信公众号配置
 * 2. 为每个公众号创建独立的 WxMpService 和消息路由器
 * 3. 监听 Redis 消息，支持运行时动态重新加载配置
 * <p>
 * 核心数据结构：
 * - routers: appid -> WxMpMessageRouter 映射，用于消息路由分发
 * - mpServices: appid -> WxMpService 映射，用于调用微信API
 * - tenants: appid -> tenantId 映射，用于多租户隔离
 *
 * @author Binary Wang
 * @author lengleng
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WxMpInitConfigRunner {

    /**
     * 消息路由器缓存
     * key: 公众号 appid
     * value: 对应的消息路由器，负责将不同类型的消息分发到对应的处理器
     */
    private static Map<String, WxMpMessageRouter> routers = Maps.newHashMap();

    /**
     * 微信公众号服务缓存
     * key: 公众号 appid
     * value: 微信公众号服务实例，封装了微信API调用能力
     */
    private static Map<String, WxMpService> mpServices = Maps.newHashMap();

    /**
     * 租户映射缓存
     * key: 公众号 appid
     * value: 租户ID，用于多租户数据隔离
     */
    private static final Map<String, Long> tenants = Maps.newHashMap();

    /**
     * 远程租户服务，用于获取所有租户信息
     */
    private final RemoteTenantService tenantService;

    /**
     * 微信公众号账户服务，用于查询公众号配置
     */
    private final WxAccountService accountService;

    /**
     * 日志处理器，记录所有消息事件日志（异步执行）
     */
    private final LogHandler logHandler;

    /**
     * 空处理器，用于不需要特殊处理的事件（如点击菜单链接）
     */
    private final NullHandler nullHandler;

    /**
     * 客服会话处理器，处理客服会话创建/关闭/转接事件
     */
    private final KfSessionHandler kfSessionHandler;

    /**
     * 门店审核通知处理器，处理门店审核结果事件
     */
    private final StoreCheckNotifyHandler storeCheckNotifyHandler;

    /**
     * 地理位置处理器，处理用户上报的地理位置信息
     */
    private final LocationHandler locationHandler;

    /**
     * 菜单点击处理器，处理自定义菜单点击事件
     */
    private final MenuHandler menuHandler;

    /**
     * 消息处理器，处理用户发送的普通消息（默认处理器）
     */
    private final MsgHandler msgHandler;

    /**
     * 取消关注处理器，处理用户取消关注公众号事件
     */
    private final UnsubscribeHandler unsubscribeHandler;

    /**
     * 关注处理器，处理用户关注公众号事件
     */
    private final SubscribeHandler subscribeHandler;

    /**
     * 扫码处理器，处理用户扫描二维码事件
     */
    private final ScanHandler scanHandler;

    /**
     * 应用启动完成事件监听
     * 异步初始化所有租户的微信公众号服务
     */
    @Async
    @Order
    @EventListener({ApplicationReadyEvent.class})
    public void WebServerInit() {
        this.initServices();
    }

    /**
     * 初始化微信公众号服务
     * <p>
     * 执行流程：
     * 1. 获取系统中所有租户
     * 2. 遍历每个租户，查询其配置的公众号列表
     * 3. 为每个公众号创建 WxMpService 和消息路由器
     * 4. 缓存 appid 与租户的映射关系
     */
    public void initServices() {
        // 获取全部租户 遍历所有租户对应的公众号列表
        List<WxAccount> accountList = new ArrayList<>();
        // @formatter:off
		RetOps.of(tenantService.list())
				.getData()
				.orElseGet(Collections::emptyList)
				.forEach(
						tenant -> TenantBroker.runAs(tenant.getId(), (id) -> accountList.addAll(accountService.list()))
				);
		// @formatter:on

        mpServices = accountList.stream().map(a -> {
            WxMpInRedisConfigStorage configStorage = new WxMpInRedisConfigStorage();
            configStorage.setAppId(a.getAppid());
            configStorage.setSecret(a.getAppsecret());
            configStorage.setToken(a.getToken());
            configStorage.setAesKey(a.getAeskey());

            WxMpService service = new WxMpServiceImpl();
            service.setWxMpConfigStorage(configStorage);
            routers.put(a.getAppid(), this.newRouter(service));
            tenants.put(a.getAppid(), a.getTenantId());
            return service;
        }).collect(Collectors.toMap(s -> s.getWxMpConfigStorage().getAppId(), a -> a, (existing, replace) -> existing));
    }

    /**
     * 创建微信消息路由器
     * <p>
     * 配置消息路由规则，将不同类型的消息/事件分发到对应的处理器：
     * - 客服会话事件 -> kfSessionHandler
     * - 门店审核事件 -> storeCheckNotifyHandler
     * - 菜单点击事件 -> menuHandler
     * - 关注/取消关注事件 -> subscribeHandler/unsubscribeHandler
     * - 地理位置事件 -> locationHandler
     * - 扫码事件 -> scanHandler
     * - 其他消息 -> msgHandler（默认处理器）
     *
     * @param wxMpService 微信公众号服务实例
     * @return 配置完成的消息路由器
     */
    private WxMpMessageRouter newRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行） #5637 大数据量瓶颈
        newRouter.rule().handler(this.logHandler).next();

        // 接收客服会话管理事件
        newRouter.rule()
                .async(false)
                .msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION)
                .handler(this.kfSessionHandler)
                .end();
        newRouter.rule()
                .async(false)
                .msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION)
                .handler(this.kfSessionHandler)
                .end();
        newRouter.rule()
                .async(false)
                .msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION)
                .handler(this.kfSessionHandler)
                .end();

        // 门店审核事件
        newRouter.rule()
                .async(false)
                .msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.POI_CHECK_NOTIFY)
                .handler(this.storeCheckNotifyHandler)
                .end();

        // 自定义菜单事件
        newRouter.rule()
                .async(false)
                .msgType(XmlMsgType.EVENT)
                .event(MenuButtonType.CLICK)
                .handler(this.menuHandler)
                .end();

        // 点击菜单连接事件
        newRouter.rule()
                .async(false)
                .msgType(XmlMsgType.EVENT)
                .event(MenuButtonType.VIEW)
                .handler(this.nullHandler)
                .end();

        // 关注事件
        newRouter.rule()
                .async(false)
                .msgType(XmlMsgType.EVENT)
                .event(EventType.SUBSCRIBE)
                .handler(this.subscribeHandler)
                .end();

        // 取消关注事件
        newRouter.rule()
                .async(false)
                .msgType(XmlMsgType.EVENT)
                .event(EventType.UNSUBSCRIBE)
                .handler(this.unsubscribeHandler)
                .end();

        // 上报地理位置事件
        newRouter.rule()
                .async(false)
                .msgType(XmlMsgType.EVENT)
                .event(EventType.LOCATION)
                .handler(this.locationHandler)
                .end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.SCAN).handler(this.scanHandler).end();

        // 默认
        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }

    /**
     * 配置 Redis 消息监听容器
     * <p>
     * 监听 mp_redis_reload_topic 频道，当收到消息时重新加载公众号配置
     * 适用场景：后台修改公众号配置后，通过发布 Redis 消息触发配置热更新
     *
     * @param redisConnectionFactory Redis 连接工厂
     * @return Redis 消息监听容器
     */
    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener((message, bytes) -> {
            log.warn("接收到重新加载公众号配置事件");
            initServices();
        }, new ChannelTopic(CacheConstants.MP_REDIS_RELOAD_TOPIC));
        return container;
    }

    public static Map<String, Long> getTenants() {
        return tenants;
    }

    public static Map<String, WxMpMessageRouter> getRouters() {
        return routers;
    }

    public static Map<String, WxMpService> getMpServices() {
        return mpServices;
    }

}
