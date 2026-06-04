/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pig.admin.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig4cloud.pig.admin.api.constant.UpmsErrorCodes;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.entity.SysSocialDetails;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.admin.mapper.SysSocialDetailsMapper;
import com.pig4cloud.pig.admin.service.SysUserService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.constant.enums.LoginTypeEnum;
import com.pig4cloud.pig.common.core.exception.CheckedException;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.data.cache.RedisUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 微信小程序手机号登录处理器。
 * <p>
 * 通过前端 wx.getPhoneNumber 返回的 code，换取用户手机号并完成登录绑定。 access_token 获取后会缓存至
 * Redis，减少对微信接口的重复调用。
 *
 * @author lengleng
 * @date 2026-05-26
 */
@Slf4j
@Component("MINI")
@AllArgsConstructor
public class MiniAppLoginHandler extends AbstractLoginHandler {

	/**
	 * 微信 access_token 默认有效期，单位秒（官方文档值）。
	 */
	private static final long ACCESS_TOKEN_DEFAULT_EXPIRES_IN = 7200L;

	/**
	 * 提前过期缓冲时间，单位秒，避免在恰好过期时使用即将失效的 token。
	 */
	private static final long ACCESS_TOKEN_EXPIRES_BUFFER = 300L;

	/**
	 * Redis 缓存 access_token 的最小 TTL，单位秒，防止因 expires_in 异常导致缓存永久有效。
	 */
	private static final long ACCESS_TOKEN_MIN_TTL = 60L;

	private final SysUserService sysUserService;

	private final SysSocialDetailsMapper sysSocialDetailsMapper;

	/**
	 * 小程序手机号登录传入 getPhoneNumber 返回的 code。
	 * <p>
	 * 该 code 不是 wx.login 返回的登录 code，只能用于服务端换取手机号，有效期 5 分钟且一次性。
	 * @param code 前端 wx.getPhoneNumber 回调中的动态令牌，不允许为空
	 * @return 用户手机号（优先返回不含国家码的纯手机号）
	 */
	@Override
	public String identify(String code) {
		if (StrUtil.isBlank(code)) {
			log.warn("小程序手机号登录 code 为空");
			throw miniAppLoginFailed();
		}

		SysSocialDetails socialDetails = getMiniAppSocialDetails();
		String accessToken = getMiniAppAccessToken(socialDetails);
		String result = postJson(String.format(SecurityConstants.MINI_APP_PHONE_NUMBER_URL, accessToken),
				JSONUtil.createObj().set("code", code).toString());
		log.debug("微信小程序手机号响应报文:{}", result);
		JSONObject resultJsonObj = JSONUtil.parseObj(result);

		checkWechatResponse(resultJsonObj, result);
		JSONObject phoneInfo = resultJsonObj.getJSONObject("phone_info");
		if (phoneInfo == null) {
			log.error("微信小程序手机号响应缺少 phone_info:{}", result);
			throw miniAppLoginFailed();
		}
		String purePhoneNumber = phoneInfo.getStr("purePhoneNumber");
		if (StrUtil.isNotBlank(purePhoneNumber)) {
			return purePhoneNumber;
		}
		String phoneNumber = phoneInfo.getStr("phoneNumber");
		if (StrUtil.isBlank(phoneNumber)) {
			log.error("微信小程序手机号响应缺少手机号:{}", result);
			throw miniAppLoginFailed();
		}
		return phoneNumber;
	}

	/**
	 * 根据手机号获取用户信息。
	 * @param phone 用户手机号
	 * @return 用户信息对象，未找到时返回null
	 */
	@Override
	public UserInfo info(String phone) {
		if (StrUtil.isBlank(phone)) {
			log.warn("小程序手机号为空，无法获取用户信息");
			return null;
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setPhone(phone);

		R<UserInfo> userInfoR = sysUserService.getUserInfo(userDTO);

		if (userInfoR.getData() == null) {
			log.info("小程序手机号不存在用户:{}", phone);
			return null;
		}

		return userInfoR.getData();
	}

	/**
	 * 绑定逻辑：将手机号写入用户记录。
	 * @param user 用户实体
	 * @param identify 微信返回的手机号
	 * @return 始终返回 true
	 */
	@Override
	public Boolean bind(SysUser user, String identify) {
		user.setPhone(identify);
		sysUserService.updateById(user);
		return true;
	}

	private SysSocialDetails getMiniAppSocialDetails() {
		SysSocialDetails condition = new SysSocialDetails();
		condition.setType(LoginTypeEnum.MINI_APP.getType());
		SysSocialDetails socialDetails = sysSocialDetailsMapper.selectOne(new QueryWrapper<>(condition));
		if (socialDetails == null || StrUtil.isBlank(socialDetails.getAppId())
				|| StrUtil.isBlank(socialDetails.getAppSecret())) {
			log.error("微信小程序密钥配置缺失");
			throw miniAppLoginFailed();
		}
		return socialDetails;
	}

	private String getMiniAppAccessToken(SysSocialDetails socialDetails) {
		String cacheKey = getAccessTokenCacheKey(socialDetails);
		String cachedAccessToken = getCachedAccessToken(cacheKey);
		if (StrUtil.isNotBlank(cachedAccessToken)) {
			return cachedAccessToken;
		}

		String result = get(String.format(SecurityConstants.MINI_APP_ACCESS_TOKEN_URL, socialDetails.getAppId(),
				socialDetails.getAppSecret()));
		log.debug("微信小程序 access_token 响应报文:{}", result);
		JSONObject resultJsonObj = JSONUtil.parseObj(result);
		checkWechatResponse(resultJsonObj, result);

		String accessToken = resultJsonObj.getStr("access_token");
		if (StrUtil.isBlank(accessToken)) {
			log.error("微信小程序 access_token 为空:{}", result);
			throw miniAppLoginFailed();
		}

		Long expiresIn = resultJsonObj.getLong("expires_in");
		long ttl = Math.max(ACCESS_TOKEN_MIN_TTL,
				(expiresIn == null ? ACCESS_TOKEN_DEFAULT_EXPIRES_IN : expiresIn) - ACCESS_TOKEN_EXPIRES_BUFFER);
		cacheAccessToken(cacheKey, accessToken, ttl);
		return accessToken;
	}

	private String getAccessTokenCacheKey(SysSocialDetails socialDetails) {
		return String.format("%sminiapp:access_token:%s", CacheConstants.GLOBALLY, socialDetails.getAppId());
	}

	/**
	 * 从 Redis 读取已缓存的 access_token，供子类在测试中覆盖。
	 * @param cacheKey Redis 键
	 * @return 缓存的 access_token，不存在时返回 null
	 */
	protected String getCachedAccessToken(String cacheKey) {
		return RedisUtils.get(cacheKey);
	}

	/**
	 * 将 access_token 写入 Redis，供子类在测试中覆盖。
	 * @param cacheKey Redis 键
	 * @param accessToken 待缓存的 access_token
	 * @param ttl 过期时间，单位秒
	 */
	protected void cacheAccessToken(String cacheKey, String accessToken, long ttl) {
		RedisUtils.set(cacheKey, accessToken, ttl);
	}

	/**
	 * 发起 HTTP GET 请求，供子类在测试中覆盖。
	 * @param url 请求地址
	 * @return 响应体字符串
	 */
	protected String get(String url) {
		return HttpUtil.get(url);
	}

	/**
	 * 发起 HTTP POST JSON 请求，供子类在测试中覆盖。
	 * @param url 请求地址
	 * @param body JSON 请求体字符串
	 * @return 响应体字符串
	 */
	protected String postJson(String url, String body) {
		return HttpRequest.post(url).header("Content-Type", "application/json").body(body).execute().body();
	}

	private void checkWechatResponse(JSONObject resultJsonObj, String rawResult) {
		Integer errorCode = resultJsonObj.getInt("errcode");
		if (errorCode != null && errorCode != 0) {
			log.error("微信小程序登录失败:{}", rawResult);
			throw miniAppLoginFailed();
		}
	}

	/**
	 * 构造小程序登录失败异常，供子类在测试中覆盖或统一替换错误信息。
	 * @return 标准小程序登录失败异常
	 */
	protected CheckedException miniAppLoginFailed() {
		return new CheckedException(MsgUtils.getMessage(UpmsErrorCodes.SYS_MINIAPP_LOGIN_FAILED));
	}

}
