package com.pig4cloud.pig.admin.config;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.admin.service.SysOauthClientDetailsService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.data.cache.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
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
     * ApplicationReadyEvent 使用 TransactionalEventListener 时启动时无法获取到事件
	 */
	@Async
	@Order
    @EventListener({ApplicationReadyEvent.class})
	public void webServerInit() {
		this.initClientDetails();
	}

	@Async
	@Order
	@TransactionalEventListener({ ClientDetailsInitEvent.class })
	public void initClientDetails() {
		log.debug("初始化客户端信息开始 ");

		clientDetailsService.list().stream()
				.filter(client -> StrUtil.isNotBlank(client.getAdditionalInformation()))
				.forEach(client -> {
					String key = String.format("%s:%s", CacheConstants.CLIENT_FLAG, client.getClientId());
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
