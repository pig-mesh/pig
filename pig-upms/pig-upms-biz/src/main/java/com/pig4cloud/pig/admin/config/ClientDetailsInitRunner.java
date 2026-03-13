package com.pig4cloud.pig.admin.config;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.admin.service.SysOauthClientDetailsService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.data.cache.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author lengleng
 * @date 2020/11/18
 * <p>
 * oauth 客户端认证参数初始化
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
public class ClientDetailsInitRunner implements InitializingBean {

	private final SysOauthClientDetailsService clientDetailsService;

	private final RedisMessageListenerContainer listenerContainer;

	/**
	 * WebServerInitializedEvent 使用 TransactionalEventListener 时启动时无法获取到事件
	 */
	@Async
	@Order
	@EventListener({ WebServerInitializedEvent.class })
	public void webServerInit() {
		this.initClientDetails();
	}

	@Async
	@Order
	@TransactionalEventListener({ ClientDetailsInitEvent.class })
	public void initClientDetails() {
		log.debug("初始化客户端信息开始 ");

		// 查询所有客户端信息 (排除客户端扩展信息为空)
		clientDetailsService.list().stream().filter(client -> {
			return StrUtil.isNotBlank(client.getAdditionalInformation());
		}).forEach(client -> {
			// 拼接key client_config_flag:clientId
			String key = String.format("%s:%s", CacheConstants.CLIENT_FLAG, client.getClientId());
			// hashkey clientId 保存客户端信息
			RedisUtils.set(key, client.getAdditionalInformation());
		});

		log.debug("初始化客户端信息结束 ");
	}

	/**
	 * 客户端刷新事件
	 */
	public static class ClientDetailsInitEvent extends ApplicationEvent {

		public ClientDetailsInitEvent(Object source) {
			super(source);
		}

	}

	/**
	 * redis 监听配置,监听 upms_redis_client_reload_topic,重新加载Redis
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		listenerContainer.addMessageListener((message, bytes) -> {
			log.warn("接收到重新Redis 重新加载客户端配置事件");
			initClientDetails();
		}, new ChannelTopic(CacheConstants.CLIENT_REDIS_RELOAD_TOPIC));
	}

}
