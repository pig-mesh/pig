package com.pig4cloud.pigx.common.api.encrypt.core;

import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 解析加密密钥
 *
 * @author L.cm
 */
public interface ISecretKeyResolver {

	/**
	 * 获取租户对应的加、解密密钥
	 * @param request HttpServletRequest
	 * @param encryptType 加解密类型
	 * @return 密钥
	 */
	String getSecretKey(HttpServletRequest request, EncryptType encryptType);

}
