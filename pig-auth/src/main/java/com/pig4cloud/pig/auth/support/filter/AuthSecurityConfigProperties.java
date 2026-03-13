package com.pig4cloud.pig.auth.support.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 安全认证配置属性类
 *
 * @author lengleng
 * @date 2025/08/06
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

	/** 是否开启密码超期强制修改密码 */
	private boolean expirePassword = false;

}
