package com.pig4cloud.pig.common.feign.core;

import feign.RequestInterceptor;
import org.springframework.http.HttpHeaders;

/**
 * Feign请求连接关闭拦截器
 * <p>
 * 用于设置HTTP连接为关闭状态
 *
 * @author lengleng
 * @date 2025/05/31
 */
public class PigFeignRequestCloseInterceptor implements RequestInterceptor {

	/**
	 * 设置连接关闭
	 * @param template 请求模板
	 */
	@Override
	public void apply(feign.RequestTemplate template) {
		template.header(HttpHeaders.CONNECTION, "close");
	}

}
