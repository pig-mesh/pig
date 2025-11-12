package com.pig4cloud.pig.common.security.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2025-11-12
 * <p>
 * CORS 跨域资源共享配置属性
 */
@Data
@ConfigurationProperties(prefix = "security.cors")
public class PigBootCorsProperties {

	/**
	 * 是否启用CORS跨域支持
	 */
	private Boolean enabled = false;

	/**
	 * 允许的源模式列表，支持通配符 例如：http://localhost:*, https://*.example.com
	 */
	private List<String> allowedOriginPatterns = new ArrayList<>(List.of("*"));

	/**
	 * 允许的请求头列表 默认允许所有请求头
	 */
	private List<String> allowedHeaders = new ArrayList<>(List.of("*"));

	/**
	 * 允许的HTTP方法列表 默认允许常用的HTTP方法
	 */
	private List<String> allowedMethods = new ArrayList<>(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

	/**
	 * 是否允许携带凭证（如Cookie） 当设置为true时，allowedOriginPatterns不能使用通配符*
	 */
	private Boolean allowCredentials = true;

	/**
	 * 应用CORS配置的路径模式 默认应用到所有路径
	 */
	private String pathPattern = "/**";

}