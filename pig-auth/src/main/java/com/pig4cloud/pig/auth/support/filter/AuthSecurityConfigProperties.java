package com.pig4cloud.pig.auth.support.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lengleng
 * @date 2020/10/4
 * <p>
 * 网关配置文件
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("security")
public class AuthSecurityConfigProperties {

	/**
	 * 是否是微服务架构
	 */
	private boolean isMicro;

	/**
	 * 网关解密登录前端密码 秘钥
	 */
	private String encodeKey;

	/**
	 * 网关不需要校验验证码的客户端
	 */
	private List<String> ignoreClients;

}
