/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.auth.support.filter;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.auth.support.core.OauthClientDetailsLoader;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.constant.enums.EncFlagTypeEnum;
import com.pig4cloud.pig.common.core.util.RepeatBodyRequestWrapper;
import com.pig4cloud.pig.common.core.util.WebUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Map;

/**
 * @author lengleng
 * @date 2019 /2/1 密码解密工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordDecoderFilter extends OncePerRequestFilter {

	private static final String PASSWORD = "password";

	private static final String KEY_ALGORITHM = "AES";

	private static final int AES_KEY_LENGTH = 16;

	private static final int SM4_HEX_KEY_LENGTH = 32;

	static {
		// 关闭hutool 强制关闭Bouncy Castle库的依赖
		SecureUtil.disableBouncyCastle();
	}

	private final AuthSecurityConfigProperties authSecurityConfigProperties;

	private final OauthClientDetailsLoader clientDetailsLoader;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// 不是登录请求，直接向下执行
		if (!StrUtil.containsAnyIgnoreCase(request.getRequestURI(), SecurityConstants.OAUTH_TOKEN_URL)) {
			chain.doFilter(request, response);
			return;
		}

		// 2. 刷新token类型，直接向下执行
		String grantType = request.getParameter("grant_type");
		if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
			chain.doFilter(request, response);
			return;
		}

		// 3. 判断客户端是否需要解密，明文传输直接向下执行
		if (!isEncClient(request)) {
			chain.doFilter(request, response);
			return;
		}

		// 将请求流转换为可多次读取的请求流
		RepeatBodyRequestWrapper requestWrapper = new RepeatBodyRequestWrapper(request);
		Map<String, String[]> parameterMap = requestWrapper.getParameterMap();

		String[] values = parameterMap.get(PASSWORD);
		if (ArrayUtil.isNotEmpty(values)) {
			// 解密密码
			String decryptPassword = decryptPassword(values[0]);
			parameterMap.put(PASSWORD, new String[] { decryptPassword });
		}
		chain.doFilter(requestWrapper, response);
	}

	/**
	 * 根据 {@code security.encode-type} 配置选择 AES 或 SM4 解密前端传入的密文。
	 * <p>
	 * 包级可见以便单元测试直接覆盖；当配置为空时按 AES 兜底，避免老配置升级后无法登录。
	 * @param password 前端加密后的密码
	 * @return 解密后的明文密码
	 * @throws IllegalArgumentException 当配置的密钥长度不符合所选算法要求
	 */
	String decryptPassword(String password) {
		AuthSecurityConfigProperties.EncodeType encodeType = authSecurityConfigProperties.getEncodeType();
		if (encodeType == null) {
			encodeType = AuthSecurityConfigProperties.EncodeType.AES;
		}
		return switch (encodeType) {
			case AES -> decryptByAes(password);
			case SM4 -> decryptBySm4(password);
		};
	}

	/**
	 * 使用 AES（CFB / NoPadding，IV 等于密钥）解密密码。
	 * @param password 前端 AES 加密后的密码
	 * @return 明文密码
	 */
	private String decryptByAes(String password) {
		String encodeKey = authSecurityConfigProperties.getEncodeKey();
		Assert.hasText(encodeKey, "AES encode-key must not be empty (AES登录密码解密密钥不能为空)");
		byte[] keyBytes = encodeKey.getBytes();
		Assert.isTrue(keyBytes.length == AES_KEY_LENGTH, "AES encode-key must be 16 bytes (AES登录密码解密密钥必须为16字节)");

		// 构建前端对应解密 AES 因子
		AES aes = new AES(Mode.CFB, Padding.NoPadding, new SecretKeySpec(keyBytes, KEY_ALGORITHM),
				new IvParameterSpec(keyBytes));
		return aes.decryptStr(password);
	}

	/**
	 * 使用国密 SM4 解密密码，密钥需为 32 位 HEX 字符串。
	 * @param password 前端 SM4 加密后的密码
	 * @return 明文密码
	 */
	private String decryptBySm4(String password) {
		String encodeKey = authSecurityConfigProperties.getEncodeKey();
		Assert.hasText(encodeKey, "SM4 encode-key must not be empty (SM4登录密码解密密钥不能为空)");
		Assert.isTrue(StrUtil.length(encodeKey) == SM4_HEX_KEY_LENGTH && HexUtil.isHexNumber(encodeKey),
				"SM4 encode-key must be 32 hex characters (SM4登录密码解密密钥必须为32位HEX字符串)");
		return SmUtil.sm4(HexUtil.decodeHex(encodeKey)).decryptStr(password);
	}

	/**
	 * 根据请求的clientId 查询客户端配置是否是加密传输
	 * @param request 请求上下文
	 * @return true 加密传输，false 明文传输
	 */
	private boolean isEncClient(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		String clientId = WebUtils.extractClientId(header).orElse(null);
		if (StrUtil.isBlank(clientId)) {
			return true;
		}

		SysOauthClientDetails clientDetails = clientDetailsLoader.getByClientId(clientId);

		// 当配置不存在时，默认需要解密
		if (clientDetails == null || StrUtil.isBlank(clientDetails.getAdditionalInformation())) {
			return true;
		}

		JSONObject information = JSONUtil.parseObj(clientDetails.getAdditionalInformation());
		return !StrUtil.equals(EncFlagTypeEnum.NO.getType(), information.getStr(CommonConstants.ENC_FLAG));
	}

}
