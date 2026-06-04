package com.pig4cloud.pig.auth.support.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 安全认证配置属性类
 *
 * @author lengleng
 * @date 2026/05/03
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
	 * 认证中心解密登录前端密码的密钥。AES 使用 16 字节字符串，SM4 使用 32 位 HEX 字符串。
	 */
	private String encodeKey;

	/**
	 * 认证中心解密登录前端密码的算法，默认使用 AES 以兼容历史配置。
	 */
	private EncodeType encodeType = EncodeType.AES;

	/**
	 * 是否开启密码超期强制修改密码，默认关闭。
	 */
	private boolean expirePassword = false;

	/**
	 * 登录密码加密算法枚举
	 * <p>
	 * AES：使用 16 字节 UTF-8 字符串作为密钥，CFB / NoPadding 模式，IV 等于密钥。<br>
	 * SM4：国密 SM4 算法，使用 32 位 HEX 字符串作为密钥（解码后 16 字节）。
	 */
	public enum EncodeType {

		/** AES 算法（默认） */
		AES,
		/** 国密 SM4 算法 */
		SM4

	}

}
