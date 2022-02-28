package com.xxl.job.admin.config;

import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * @author lengleng
 * @date 2022/2/28
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class XxlJobInitConfiguration {

	private final XxlJobAdminConfig xxlJobAdminConfig;

	@SneakyThrows
	@EventListener({ WebServerInitializedEvent.class })
	public void init() {
		xxlJobAdminConfig.getXxlJobScheduler().init();
	}

}
