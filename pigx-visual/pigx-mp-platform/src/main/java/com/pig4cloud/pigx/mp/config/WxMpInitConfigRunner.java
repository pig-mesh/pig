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
import org.springframework.data.redis.core.RedisTemplate;
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
 * @author Binary Wang
 * @author lengleng
 * <p>
 * 微信公众号基础配置，初始化配置
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WxMpInitConfigRunner {

	/**
	 * 保存 appid-router 的对应关系
	 */
	private static Map<String, WxMpMessageRouter> routers = Maps.newHashMap();

	/**
	 * 保存 appid-mpservice 的对应关系
	 */
	private static Map<String, WxMpService> mpServices = Maps.newHashMap();

	/**
	 * 保存 appid-tenantId 的对应关系
	 */
	private static final Map<String, Long> tenants = Maps.newHashMap();

	private final RemoteTenantService tenantService;

	private final WxAccountService accountService;

	private final LogHandler logHandler;

	private final NullHandler nullHandler;

	private final KfSessionHandler kfSessionHandler;

	private final StoreCheckNotifyHandler storeCheckNotifyHandler;

	private final LocationHandler locationHandler;

	private final MenuHandler menuHandler;

	private final MsgHandler msgHandler;

	private final UnsubscribeHandler unsubscribeHandler;

	private final SubscribeHandler subscribeHandler;

	private final ScanHandler scanHandler;

	private final RedisTemplate redisTemplate;

	@Async
	@Order
	@EventListener({ ApplicationReadyEvent.class })
	public void WebServerInit() {
		this.initServices();
	}

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
			WxMpInRedisConfigStorage configStorage = new WxMpInRedisConfigStorage(redisTemplate);
			configStorage.setAppId(a.getAppid());
			configStorage.setSecret(a.getAppsecret());
			configStorage.setToken(a.getToken());
			configStorage.setAesKey(a.getAeskey());

			WxMpService service = new WxMpServiceImpl();
			service.setWxMpConfigStorage(configStorage);
			routers.put(a.getAppid(), this.newRouter(service));
			tenants.put(a.getAppid(), a.getTenantId());
			return service;
		}).collect(Collectors.toMap(s -> s.getWxMpConfigStorage().getAppId(), a -> a));
	}

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
	 * redis 监听配置,监听 mp_redis_reload_topic,重新加载配置
	 * @param redisConnectionFactory redis 配置
	 * @return
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
