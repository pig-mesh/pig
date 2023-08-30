package com.pig4cloud.pigx.common.api.encrypt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * api 签名配置类
 *
 * @author licoy.cn
 * @author L.cm
 * @version 2018/9/6
 */
@Getter
@Setter
@ConfigurationProperties(ApiEncryptProperties.PREFIX)
public class ApiEncryptProperties {

	/**
	 * 前缀
	 */
	public static final String PREFIX = "security.api.encrypt";

	/**
	 * 是否开启 api 签名
	 */
	private boolean enable = true;

	/**
	 * url的参数签名，传递的参数名。例如：/user?data=签名后的数据
	 */
	private String paramName = "encryption";

	/**
	 * body 内容 json key, 默认：encryption
	 */
	private String bodyJsonKey = "encryption";

	/**
	 * aes 密钥
	 */
	private String aesKey;

	/**
	 * des 密钥
	 */
	private String desKey;

	/**
	 * sm4 密钥
	 */
	private String sm4Key;

	/**
	 * rsa 私钥
	 */
	private String rsaPrivateKey;

}
