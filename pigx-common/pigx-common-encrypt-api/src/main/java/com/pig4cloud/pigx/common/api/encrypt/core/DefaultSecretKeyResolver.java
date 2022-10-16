package com.pig4cloud.pigx.common.api.encrypt.core;

import com.pig4cloud.pigx.common.api.encrypt.config.ApiEncryptProperties;
import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认的密钥处理器
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class DefaultSecretKeyResolver implements ISecretKeyResolver {

	private final ApiEncryptProperties properties;

	@Override
	public String getSecretKey(HttpServletRequest request, EncryptType encryptType) {
		switch (encryptType) {
			case DES:
				return properties.getDesKey();
			case RSA:
				return properties.getRsaPrivateKey();
			default:
				return properties.getAesKey();
		}
	}

}
