package com.pig4cloud.pigx.common.websocket.config;

import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;

/**
 * SockJsService 配置类
 *
 * @author hccake
 */
public interface SockJsServiceConfigurer {

	/**
	 * 配置 sockjs 相关
	 * @param sockJsServiceRegistration sockJsService 注册类
	 */
	void config(SockJsServiceRegistration sockJsServiceRegistration);

}
